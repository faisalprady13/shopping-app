import {StrictMode, useEffect, useState} from 'react';
import {createRoot} from 'react-dom/client'
import './App.css'
import './index.css'
import Detailseite from './components/Detailseite'
import Footer from './components/Footer'
import Header from './components/Header'
import Listenseite from './components/Listenseite'
import Startseite from './components/Startseite'
import {
  type Screen,
  type LoginDto,
  type RegisterDto,
  type SafeUser,
  type ShoppingItem,
  type ShoppingList,
  type ShoppingListDto,
  Status,
  type User,
} from './types';
import axios from 'axios';
import AddListSeite from "./components/AddListSeite.tsx";

const storedUserKey = 'shopping-app-user'

type StoredUser = Pick<SafeUser, 'id' | 'name' | 'email'>

const readInitialUser = (): StoredUser | null => {
  const params = new URLSearchParams(window.location.search)
  const oauthUserId = params.get('userId')
  const oauthName = params.get('name')
  const oauthEmail = params.get('email') ?? ''

  if (oauthUserId && oauthName) {
    const oauthUser = { id: oauthUserId, name: oauthName, email: oauthEmail }
    localStorage.setItem(storedUserKey, JSON.stringify(oauthUser))
    return oauthUser
  }

  const storedUser = localStorage.getItem(storedUserKey)
  if (!storedUser) {
    return null
  }

  try {
    const parsedUser = JSON.parse(storedUser) as StoredUser
    return parsedUser.id && parsedUser.name ? parsedUser : null
  } catch {
    localStorage.removeItem(storedUserKey)
    return null
  }
}

const initialLists: ShoppingList[] = [
  {
    id: '1',
    name: 'List 1',
    date: '29.06.2026',
    products: [
    ],
  }
]

export function App() {
  const [initialUser] = useState<StoredUser | null>(() => readInitialUser())
  const [screen, setScreen] = useState<Screen>(initialUser ? 'lists' : 'start')
  const [username, setUsername] = useState(initialUser?.name ?? '')
  const [email, setEmail] = useState(initialUser?.email ?? '')
  const [shoppingLists, setShoppingLists] = useState<ShoppingList[]>([])
  const [selectedListId, setSelectedListId] = useState<string|null>(null)
  const [errorLog, setErrorLog] = useState<string>("")
  const [listName, setListName] = useState<string>("")
  const [userId, setUserId] = useState<string>(initialUser?.id ?? "")
  const [processingList, setProcessingList] = useState<ShoppingList>(initialLists[0]);

  const selectedList = shoppingLists.find((list) => list.id === selectedListId)
  const isLoggedIn = screen !== 'start'

  function loadAllLists (usrId: string){
    axios.get<ShoppingList[]>("/api/lists/all/" + usrId)
         .then( (response) => {
           if(response.data) {
              setShoppingLists(response.data)
              }
             }
         )
         .catch( (e) => setErrorLog(e.message) );
  }

  const persistUser = (user: StoredUser) => {
    setUsername(user.name)
    setEmail(user.email)
    setUserId(user.id)
    localStorage.setItem(storedUserKey, JSON.stringify(user))
    setErrorLog('')
    setScreen('lists')
  }

  useEffect(() => {
    if (window.location.search) {
      window.history.replaceState({}, document.title, window.location.pathname)
    }
  }, [])

  useEffect(() => {
    if (!userId) {
      return
    }

    loadAllLists(userId)
  }, [userId])

  const handleError = (errorMessage: string) => {
    setErrorLog(errorMessage)
  }

  const resolveAuthError = (error: unknown) => {
    if (axios.isAxiosError(error)) {
      if (error.response?.status === 401) {
        return 'E-Mail oder Passwort ist falsch.'
      }
      if (error.response?.status === 409) {
        return 'Diese E-Mail ist bereits registriert.'
      }
      if (error.response?.status === 400) {
        return error.response.data?.detail ?? 'Bitte pruefe deine Eingaben.'
      }
      if (error.status === 502) {
        return 'Keine Verbindung zum Backend!'
      }
    }

    return 'Anmeldung fehlgeschlagen.'
  }

  const handleLogin = (loginDto: LoginDto) => {
    axios.post<SafeUser>('/api/auth/login', loginDto)
      .then((response) => persistUser(response.data))
      .catch((error_) => setErrorLog(resolveAuthError(error_)))
  }

  const handleRegister = (registerDto: RegisterDto) => {
    axios.post<SafeUser>('/api/auth/register', registerDto)
      .then((response) => persistUser(response.data))
      .catch((error_) => setErrorLog(resolveAuthError(error_)))
  }

  const handleLogout = () => {
    setUsername('')
    setEmail('')
    setUserId('')
    setShoppingLists([])
    localStorage.removeItem(storedUserKey)
    setScreen('start')
  }

  const handleAddList = () => {
    setListName("NEW")
    setScreen('add')
  }

  const onHandleSubmittedList = (shopListName: string) => {
    const actualUser: User = {id: userId}
    const shoppingListDto: ShoppingListDto = {
      name: shopListName,
      user: actualUser
    }
    let newList: ShoppingList;

    axios.post('api/lists', shoppingListDto)
          .then( (response) => {
            newList= response.data;
            const updatedLists = [newList, ...shoppingLists];
            setShoppingLists(updatedLists);
            setSelectedListId(newList.id);
            setScreen('lists');
          })
          .catch( (error_) => setErrorLog(error_) );
  }

  const handleOpenList = (listId: string) => {
    axios.get("api/lists/" + listId)
          .then( (response) => {
            setProcessingList(response.data)
            setSelectedListId(listId);
            setScreen('details');
          })
          .catch( (error_) => setErrorLog(error_) );
  }

  const handleAddItem = async (productName: string, quantity: number) => {
    const trimmedName = productName.trim();

    if (trimmedName === '' || !selectedList) {
      return;
    }

    const newItem: ShoppingItem = (
      await axios.post('/api/lists/add-product', {
        name: trimmedName,
        quantity: quantity || 1,
        status: Status.OPEN,
        shoppingListId: selectedList.id,
      })
    ).data;

    const updatedLists = shoppingLists.map((list) => {
      if (list.id !== selectedList.id) {
        return list;
      }
      setProcessingList({
        ...list,
        products: [...list.products, newItem],
      });

      return {
        ...list,
        products: [...list.products, newItem],
      };
    });
    setShoppingLists(updatedLists);
  };

  const handleToggleItem = (itemId: string) => {
    if (!selectedList) {
      return
    }

    let updatedItem: ShoppingItem;

    const updatedLists = shoppingLists.map((list) => {
      if (list.id !== selectedList.id) {
        return list
      }

      const updatedItems = list.products.map((item) => {
        if (item.id !== itemId) {
          return item
        }
        updatedItem={
          ...item,
          status: item.status===Status.OPEN?Status.CLOSED:Status.OPEN
        }
        axios.put("/api/lists/update-product", {...updatedItem,shoppingListId:selectedList.id})
        return updatedItem;
      })

      setProcessingList({
        ...list,
        products: updatedItems,
      })

      return {
        ...list,
        products: updatedItems,
      }
    })
    setShoppingLists(updatedLists)
  }

  const handleDeleteItem = (itemId: string) => {
    if (!selectedList) {
      return
    }

    const updatedLists = shoppingLists.map((list) => {
      if (list.id !== selectedList.id) {
        return list
      }

      const remainingItems = list.products.filter((item) => item.id !== itemId)

      setProcessingList({
        ...list,
        products: remainingItems,
      })
      return {
        ...list,
        products: remainingItems,
      }
    })

    setShoppingLists(updatedLists)
    axios.delete(`/api/lists/remove-product/${itemId}`)
  }

  let page = <Startseite onLogin={handleLogin} onRegister={handleRegister} />

  if (screen === 'lists') {
    page = (
      <Listenseite
        lists={shoppingLists}
        onAddList={handleAddList}
        onOpenList={handleOpenList}
      />
    )
  }

  if (screen === 'details' && selectedList) {
    page = (
      <Detailseite
        shoppingList={processingList}
        onBack={() => setScreen('lists')}
        onAddItem={handleAddItem}
        onToggleItem={handleToggleItem}
        onDeleteItem={handleDeleteItem}
        onError={handleError}
      />
    );
  }

  if (screen === 'add'){
    page = (
      <AddListSeite
        listName={listName}
        onBack={() => setScreen('lists')}
        submitList={onHandleSubmittedList}
        onError={handleError}
      />
    );
  }

  return (
    <div className="app-shell">
      <Header username={username} email={email} isLoggedIn={isLoggedIn} onLogout={handleLogout} />

      <main className="app-content">{page}</main>

      <Footer errormessage={errorLog} />
    </div>
  )
}

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <App />
  </StrictMode>,
)
