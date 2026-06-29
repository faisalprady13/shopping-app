import { useState } from 'react'
import type { FormEvent } from 'react'
import Footer from './Footer'
import Header, { PageHeader } from './Header'

type Screen = 'start' | 'lists' | 'details'

type ShoppingItem = {
  id: number
  name: string
  quantity: string
  completed: boolean
}

type ShoppingList = {
  id: number
  title: string
  createdAt: string
  items: ShoppingItem[]
}

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

const Body = () => {
  // App navigation
  const [screen, setScreen] = useState<Screen>('start')

  // User and shopping list data
  const [username, setUsername] = useState('')
  const [lists, setLists] = useState<ShoppingList[]>(initialLists)
  const [activeListId, setActiveListId] = useState(initialLists[0].id)

  // Detail page form fields
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
          <section className="wire-panel start-panel" aria-labelledby="start-title">
            <PageHeader title="Startseite" subtitle="Gib deinen Namen ein und starte deine Listen." />

            <form className="name-form" onSubmit={login}>
              <label htmlFor="username">Name</label>
              <div className="form-row">
                <input
                  id="username"
                  value={username}
                  onChange={(event) => setUsername(event.target.value)}
                  autoComplete="name"
                />
                <button type="submit">Start</button>
              </div>
            </form>
          </section>
        ) : null}

        {screen === 'lists' ? (
          <section className="wire-panel list-panel" aria-labelledby="lists-title">
            <PageHeader title="Listenseite" subtitle="Wähle eine Liste oder erstelle eine neue." />

            <div className="top-bar">
              <button type="button" onClick={addList}>
                + neue List
              </button>
            </div>

            <div className="list-stack">
              {lists.map((list) => {
                const completedCount = list.items.filter((item) => item.completed).length
                const status =
                  list.items.length === 0
                    ? 'leer'
                    : `${completedCount} von ${list.items.length} erledigt`

                return (
                  <div className="shopping-list-card" key={list.id}>
                    <button className="list-open-button" type="button" onClick={() => openList(list.id)}>
                      <span>
                        <strong>{list.title}</strong>
                        <small>{status}</small>
                      </span>
                      <time>{list.createdAt}</time>
                    </button>
                    <button className="delete-button" type="button" onClick={() => deleteList(list.id)}>
                      del
                    </button>
                  </div>
                )
              })}
            </div>
          </section>
        ) : null}

        {screen === 'details' && activeList ? (
          <section className="wire-panel details-panel" aria-labelledby="details-title">
            <div className="top-bar details-bar">
              <strong>{activeList.title}</strong>
              <span id="details-title">Detailseite</span>
              <button type="button" onClick={() => setScreen('lists')}>
                back
              </button>
            </div>

            <div className="details-content">
              <ul className="items-list" aria-label={`${activeList.title} Produkte`}>
                {activeList.items.map((item) => (
                  <li className={item.completed ? 'completed' : ''} key={item.id}>
                    <button
                      className="check-button"
                      type="button"
                      aria-label={`${item.name} als erledigt markieren`}
                      onClick={() => toggleItem(item.id)}
                    >
                      {item.completed ? '✓' : ''}
                    </button>
                    <span>
                      <strong>{item.name}</strong>
                      <small>Menge: {item.quantity}</small>
                    </span>
                    <button className="delete-button" type="button" onClick={() => deleteItem(item.id)}>
                      del
                    </button>
                  </li>
                ))}
              </ul>

              <form className="item-form" onSubmit={addItem}>
                <label htmlFor="product-name">Produktname</label>
                <label htmlFor="product-quantity">Menge</label>
                <input
                  id="product-name"
                  value={productName}
                  onChange={(event) => setProductName(event.target.value)}
                />
                <input
                  id="product-quantity"
                  value={quantity}
                  onChange={(event) => setQuantity(event.target.value)}
                />
                <button type="submit">add item</button>
              </form>
            </div>
          </section>
        ) : null}
      </main>

      <Footer />
    </div>
  )
}

export default Body
