import { StrictMode } from 'react'
import { useState } from 'react'
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
  const [lists, setLists] = useState<ShoppingList[]>(initialLists)
  const [activeListId, setActiveListId] = useState(initialLists[0].id)
  const [productName, setProductName] = useState('')
  const [quantity, setQuantity] = useState('1')

  const activeList = lists.find((list) => list.id === activeListId)
  const isLoggedIn = screen !== 'start'

  const login = (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault()

    if (username.trim() === '') {
      return
    }

    setScreen('lists')
  }

  const logout = () => {
    setUsername('')
    setScreen('start')
  }

  const addList = () => {
    const nextNumber = lists.length + 1
    const today = new Intl.DateTimeFormat('de-DE').format(new Date())

    const newList: ShoppingList = {
      id: Date.now(),
      title: `List ${nextNumber}`,
      createdAt: today,
      items: [],
    }

    const updatedLists = [newList, ...lists]

    setLists(updatedLists)
    setActiveListId(newList.id)
    setScreen('details')
  }

  const openList = (listId: number) => {
    setActiveListId(listId)
    setScreen('details')
  }

  const addItem = (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault()

    const trimmedName = productName.trim()
    const trimmedQuantity = quantity.trim()

    if (trimmedName === '' || !activeList) {
      return
    }

    const newItem: ShoppingItem = {
      id: Date.now(),
      name: trimmedName,
      quantity: trimmedQuantity || '1',
      completed: false,
    }

    const updatedLists = lists.map((list) => {
      if (list.id !== activeList.id) {
        return list
      }

      return {
        ...list,
        items: [...list.items, newItem],
      }
    })

    setLists(updatedLists)
    setProductName('')
    setQuantity('1')
  }

  const toggleItem = (itemId: number) => {
    if (!activeList) {
      return
    }

    const updatedLists = lists.map((list) => {
      if (list.id !== activeList.id) {
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

    setLists(updatedLists)
  }

  const deleteItem = (itemId: number) => {
    if (!activeList) {
      return
    }

    const updatedLists = lists.map((list) => {
      if (list.id !== activeList.id) {
        return list
      }

      const remainingItems = list.items.filter((item) => item.id !== itemId)

      return {
        ...list,
        items: remainingItems,
      }
    })

    setLists(updatedLists)
  }

  const deleteList = (listId: number) => {
    const remainingLists = lists.filter((list) => list.id !== listId)

    if (activeListId === listId && remainingLists.length > 0) {
      setActiveListId(remainingLists[0].id)
    }

    setLists(remainingLists)
  }

  return (
    <div className="app-shell">
      <Header username={username} isLoggedIn={isLoggedIn} onLogout={logout} />

      <main className="app-content">
        {screen === 'start' ? (
          <Startseite username={username} onUsernameChange={setUsername} onLogin={login} />
        ) : null}

        {screen === 'lists' ? (
          <Listenseite lists={lists} onAddList={addList} onOpenList={openList} onDeleteList={deleteList} />
        ) : null}

        {screen === 'details' && activeList ? (
          <Detailseite
            activeList={activeList}
            productName={productName}
            quantity={quantity}
            onProductNameChange={setProductName}
            onQuantityChange={setQuantity}
            onBack={() => setScreen('lists')}
            onAddItem={addItem}
            onToggleItem={toggleItem}
            onDeleteItem={deleteItem}
          />
        ) : null}
      </main>

      <Footer />
    </div>
  )
}

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <App />
  </StrictMode>,
)
