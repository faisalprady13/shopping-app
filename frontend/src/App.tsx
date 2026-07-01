import { StrictMode, useState } from 'react'
import type { FormEvent } from 'react'
import { createRoot } from 'react-dom/client'
import './App.css'
import './index.css'
import Detailseite from './components/Detailseite'
import Footer from './components/Footer'
import Header from './components/Header'
import Listenseite from './components/Listenseite'
import Startseite from './components/Startseite'
import type { Screen, ShoppingItem, ShoppingList } from './types'

const initialLists: ShoppingList[] = [
  {
    id: 1,
    title: 'List 1',
    createdAt: '29.06.2026',
    items: [
      { id: 1, name: 'Milch', quantity: '1', completed: false },
      { id: 2, name: 'Brot', quantity: '2', completed: true },
      { id: 3, name: 'Aepfel', quantity: '6', completed: false },
    ],
  },
  {
    id: 2,
    title: 'List 2',
    createdAt: '28.06.2026',
    items: [
      { id: 4, name: 'Reis', quantity: '1', completed: false },
      { id: 5, name: 'Tomaten', quantity: '4', completed: false },
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

  const selectedList = shoppingLists.find((list) => list.id === selectedListId)
  const isLoggedIn = screen !== 'start'

  const handleLogin = (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault()

    if (username.trim() === '') {
      return
    }

    setScreen('lists')
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
      title: `List ${nextNumber}`,
      createdAt: today,
      items: [],
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
      completed: false,
    }

    const updatedLists = shoppingLists.map((list) => {
      if (list.id !== selectedList.id) {
        return list
      }

      return {
        ...list,
        items: [...list.items, newItem],
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

      const updatedItems = list.items.map((item) => {
        if (item.id !== itemId) {
          return item
        }

        return {
          ...item,
          completed: !item.completed,
        }
      })

      return {
        ...list,
        items: updatedItems,
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

      const remainingItems = list.items.filter((item) => item.id !== itemId)

      return {
        ...list,
        items: remainingItems,
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

      <Footer />
    </div>
  )
}

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <App />
  </StrictMode>,
)
