import {PageHeader} from './Header'
import {type ShoppingList, Status} from '../types'

type ListenseiteProps = {
  lists: ShoppingList[],
  onAddList: () => void,
  onOpenList: (listId: string) => void
}

const Listenseite = ({ lists, onAddList, onOpenList }: ListenseiteProps) => {
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
          const completedCount = list.products.filter((item) => item.status===Status.CLOSED).length
          const status =
            list.products.length === 0 ? 'leer' : `${completedCount} von ${list.products.length} erledigt`

          return (
            <div className="shopping-list-card" key={list.id}>
              <button className="list-open-button" type="button" onClick={() => onOpenList(list.id)}>
                <span>
                  <strong className={
                      (list.products.length > 0 && list.products.some(product=>product.status===Status.OPEN))?"":"completedList"}>{list.name}</strong>
                  <small>{status}</small>
                </span>
                <time>{list.date}</time>
              </button>
            </div>
          )
        })}
      </div>
    </section>
  )
}

export default Listenseite
