import { StrictMode, useState } from 'react';
import type { FormEvent } from 'react'
import { createRoot } from 'react-dom/client'
import './App.css'
import './index.css'
import Detailseite from './components/Detailseite'
import Footer from './components/Footer'
import Header from './components/Header'
import Listenseite from './components/Listenseite'
import Startseite from './components/Startseite'
import type { Screen, ShoppingItem, ShoppingList, UserDto } from './types';
import axios from 'axios';

const initialLists: ShoppingList[] = [
  {
    id: 1,
    name: 'List 1',
    date: '29.06.2026',
    products: [
      { id: 1, name: 'Milch', quantity: '1', status: false },
      { id: 2, name: 'Brot', quantity: '2', status: true },
      { id: 3, name: 'Aepfel', quantity: '6', status: false },
    ],
  },
  {
    id: 2,
    name: 'List 2',
    date: '28.06.2026',
    products: [
      { id: 4, name: 'Reis', quantity: '1', status: false },
      { id: 5, name: 'Tomaten', quantity: '4', status: false },
    ],
  },
]

export function App() {
  const [screen, setScreen] = useState<Screen>('start')
  const [username, setUsername] = useState('')
  const [shoppingLists, setShoppingLists] = useState<ShoppingList[]>(initialLists)
  const [selectedListId, setSelectedListId] = useState(initialLists[0].id)
  const [productName, setProductName] = useState('')
  const [quantity, setQuantity] = useState('1')
  const [errorlog, setErrorlog] = useState<string>("")

  const selectedList = shoppingLists.find((list) => list.id === selectedListId)
  const isLoggedIn = screen !== 'start'

  function loadAllLists (usrId: string){
    axios.get("/api/lists/all/" + usrId)
         .then( (response) =>
            setShoppingLists(response.data)
         )
         .catch( (error_) => setErrorlog(error_) );
  }

  const handleLogin = (event: FormEvent<HTMLFormElement>) => {
    const userDto: UserDto= {"name": username}

    event.preventDefault()

    if (username.trim() === '') {
      return
    }

    axios
      .post('/api/user', userDto)
      .then( (response) => {
        setUsername(response.data.name);
        loadAllLists(response.data.id);
        setScreen('lists');
      })
      .catch( (error_) => {
        if(error_.status === 502){
          setErrorlog("Keine Verbindung zum Backend!")
        } else {
          console.log(error_);
        }
      });
  }

  const handleLogout = () => {
    setUsername('')
    setScreen('start')
  }

  const handleAddList = () => {
    const nextNumber = shoppingLists.length + 1
    const today = new Intl.DateTimeFormat('de-DE').format(new Date())

    const newList: ShoppingList = {
      id: Date.now(),
      name: `List ${nextNumber}`,
      date: today,
      products: [],
    }

    const updatedLists = [newList, ...shoppingLists]

    setShoppingLists(updatedLists)
    setSelectedListId(newList.id)
    setScreen('details')
  }

  const handleOpenList = (listId: number) => {
    setSelectedListId(listId)
    setScreen('details')
  }

  const handleAddItem = (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault()

    const trimmedName = productName.trim()
    const trimmedQuantity = quantity.trim()

    if (trimmedName === '' || !selectedList) {
      return
    }

    const newItem: ShoppingItem = {
      id: Date.now(),
      name: trimmedName,
      quantity: trimmedQuantity || '1',
      status: false,
    }

    const updatedLists = shoppingLists.map((list) => {
      if (list.id !== selectedList.id) {
        return list
      }

      return {
        ...list,
        products: [...list.products, newItem],
      }
    })

    setShoppingLists(updatedLists)
    setProductName('')
    setQuantity('1')
  }

  const handleToggleItem = (itemId: number) => {
    if (!selectedList) {
      return
    }

    const updatedLists = shoppingLists.map((list) => {
      if (list.id !== selectedList.id) {
        return list
      }

      const updatedItems = list.products.map((item) => {
        if (item.id !== itemId) {
          return item
        }

        return {
          ...item,
          status: !item.status,
        }
      })

      return {
        ...list,
        products: updatedItems,
      }
    })

    setShoppingLists(updatedLists)
  }

  const handleDeleteItem = (itemId: number) => {
    if (!selectedList) {
      return
    }

    const updatedLists = shoppingLists.map((list) => {
      if (list.id !== selectedList.id) {
        return list
      }

      const remainingItems = list.products.filter((item) => item.id !== itemId)

      return {
        ...list,
        products: remainingItems,
      }
    })

    setShoppingLists(updatedLists)
  }

  const handleDeleteList = (listId: number) => {
    const remainingLists = shoppingLists.filter((list) => list.id !== listId)

    if (selectedListId === listId && remainingLists.length > 0) {
      setSelectedListId(remainingLists[0].id)
    }

    setShoppingLists(remainingLists)
  }

  let page = <Startseite username={username} onUsernameChange={setUsername} onLogin={handleLogin} />

  if (screen === 'lists') {
    page = (
      <Listenseite
        lists={shoppingLists}
        onAddList={handleAddList}
        onOpenList={handleOpenList}
        onDeleteList={handleDeleteList}
      />
    )
  }

  if (screen === 'details' && selectedList) {
    page = (
      <Detailseite
        shoppingList={selectedList}
        productName={productName}
        quantity={quantity}
        onProductNameChange={setProductName}
        onQuantityChange={setQuantity}
        onBack={() => setScreen('lists')}
        onAddItem={handleAddItem}
        onToggleItem={handleToggleItem}
        onDeleteItem={handleDeleteItem}
      />
    )
  }

  return (
    <div className="app-shell">
      <Header username={username} isLoggedIn={isLoggedIn} onLogout={handleLogout} />

      <main className="app-content">{page}</main>

      <Footer errormessage={errorlog} />
    </div>
  )
}

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <App />
  </StrictMode>,
)
