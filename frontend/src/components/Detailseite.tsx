import type { FormEvent } from 'react'
import type { ShoppingList } from '../types'

type DetailseiteProps = {
  shoppingList: ShoppingList
  productName: string
  quantity: string
  onProductNameChange: (productName: string) => void
  onQuantityChange: (quantity: string) => void
  onBack: () => void
  onAddItem: (event: FormEvent<HTMLFormElement>) =>Promise<void>
  onToggleItem: (itemId: string) => void
  onDeleteItem: (itemId: string) => void
}

const Detailseite = ({
  shoppingList,
  productName,
  quantity,
  onProductNameChange,
  onQuantityChange,
  onBack,
  onAddItem,
  onToggleItem,
  onDeleteItem,
}: DetailseiteProps) => {
  return (
    <section className="wire-panel details-panel" aria-labelledby="details-title">
      <header className="page-title details-title">
        <h1 id="details-title">Detailseite</h1>
        <p>Fügen Sie einen Produktnamen hinzu, löschen, wählen oder dessen Auswahl auf.</p>
      </header>

      <div className="details-list-row">
        <strong>{shoppingList.name}</strong>
        <button type="button" onClick={onBack}>
          back
        </button>
      </div>

      <div className="details-content">
        <ul className="items-list" aria-label={`${shoppingList.name} Produkte`}>
          {shoppingList.products.map((item) => (
            <li className={item.status ? 'completed' : ''} key={item.id}>
              <button
                className="check-button"
                type="button"
                aria-label={`${item.name} als erledigt markieren`}
                onClick={() => onToggleItem(item.id)}
              >
                {item.status ? '✓' : ''}
              </button>
              <span>
                <strong>{item.name}</strong>
                <small>Menge: {item.quantity}</small>
              </span>
              <button className="delete-button" type="button" onClick={() => onDeleteItem(item.id)}>
                del
              </button>
            </li>
          ))}
        </ul>

        <form className="item-form" onSubmit={onAddItem}>
          <label htmlFor="product-name">Produktname</label>
          <label htmlFor="product-quantity">Menge</label>
          <input
            id="product-name"
            value={productName}
            onChange={(event) => onProductNameChange(event.target.value)}
          />
          <input
            id="product-quantity"
            value={quantity}
            onChange={(event) => onQuantityChange(event.target.value)}
          />
          <button type="submit">add item</button>
        </form>
      </div>
    </section>
  )
}

export default Detailseite
