import type {FormEvent} from 'react'
import {StrictMode, useState} from 'react';
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
  type ShoppingItem,
  type ShoppingList,
  type ShoppingListDto,
  Status,
  type User,
  type UserDto
} from './types';
import axios from 'axios';
import AddListSeite from "./components/AddListSeite.tsx";


export function App() {
  const [screen, setScreen] = useState<Screen>('start')
  const [username, setUsername] = useState('')
  const [shoppingLists, setShoppingLists] = useState<ShoppingList[]>([])
  const [selectedListId, setSelectedListId] = useState<string|null>(null)
  const [productName, setProductName] = useState('')
  const [quantity, setQuantity] = useState('1')
  const [errorlog, setErrorlog] = useState<string>("")
  const [listName, setListName] = useState<string>("")
  const [userId, setUserId] = useState<string>("")

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
         .catch( (e) => setErrorlog(e.message) );
  }

  const handleLogin = (event: FormEvent<HTMLFormElement>) => {
    const userDto: UserDto= {"name": username}

    event.preventDefault()

    if (username.trim() === '') {
      return
    }

    axios.post('/api/user', userDto)
          .then( (response) => {
            setUsername(response.data.name);
            setUserId(response.data.id);
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
          .catch((error_) => setErrorlog(error_));
  }

  const handleOpenList = (listId: string) => {
    setSelectedListId(listId)
    setScreen('details')
  }

  const handleAddItem =async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault()

    const trimmedName = productName.trim()
    const trimmedQuantity = quantity.trim()

    if (trimmedName === '' || !selectedList) {
      return
    }

    const newItem: ShoppingItem =
        (await axios.post("/api/lists/add-product", {
          name: trimmedName,
          quantity: trimmedQuantity || '1',
          status: Status.OPEN,
          shoppingListId: selectedList.id
        })).data;

    const updatedLists = shoppingLists.map((list) => {
      if (list.id !== selectedList.id) {
        return list
      }

      return {
        ...list,
        products: [...list.products, newItem],
      }
    })
    console.log(updatedLists)
    setShoppingLists(updatedLists)
    setProductName('')
    setQuantity('1')

  }

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
          status: item.status===Status.OPEN?Status.OPEN:Status.CLOSED,
        }
        axios.put("/api/lists/update-product", updatedItem)
        return updatedItem;
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

      return {
        ...list,
        products: remainingItems,
      }
    })

    setShoppingLists(updatedLists)
    axios.delete(`/api/lists/remove-product/${itemId}`)
  }

  const handleDeleteList = (listId: string) => {
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

  if (screen === 'add'){
    page = (
      <AddListSeite
        listName={listName}
        onBack={() => setScreen('lists')}
        submitList={onHandleSubmittedList}
      />
    );
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
