import { PageHeader } from './Header'
import type { ShoppingList } from '../types'

type ListenseiteProps = {
  lists: ShoppingList[],
  onAddList: () => void,
  onOpenList: (listId: number) => void,
  onDeleteList: (listId: number) => void
}

const Listenseite = ({ lists, onAddList, onOpenList, onDeleteList }: ListenseiteProps) => {
  return (
    <section className="wire-panel list-panel" aria-labelledby="lists-title">
      <PageHeader title="Listenseite" subtitle="Wähle eine Liste oder erstelle eine neue." />

      <div className="top-bar">
        <button type="button" onClick={onAddList}>
          + neue List
        </button>
      </div>

      <div className="list-stack">
        {lists.map((list) => {
          const completedCount = list.products.filter((item) => item.status).length
          const status =
            list.products.length === 0 ? 'leer' : `${completedCount} von ${list.products.length} erledigt`

          return (
            <div className="shopping-list-card" key={list.id}>
              <button className="list-open-button" type="button" onClick={() => onOpenList(list.id)}>
                <span>
                  <strong>{list.name}</strong>
                  <small>{status}</small>
                </span>
                <time>{list.date}</time>
              </button>
              <button className="delete-button" type="button" onClick={() => onDeleteList(list.id)}>
                del
              </button>
            </div>
          )
        })}
      </div>
    </section>
  )
}

export default Listenseite
