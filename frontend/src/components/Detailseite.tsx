import  { useEffect } from 'react'
import {type ShoppingList, Status} from '../types'
import { useForm } from 'react-hook-form';

type FormValues = {
  productName: string,
  quantity: number
}

type DetailseiteProps = {
  shoppingList: ShoppingList,
  onBack: () => void;
  onAddItem: (productName: string, quantity: number) => Promise<void>,
  onToggleItem: (itemId: string) => void,
  onDeleteItem: (itemId: string) => void,
  onError: (message: string) => void
}

const Detailseite = ({
  shoppingList,
  onBack,
  onAddItem,
  onToggleItem,
  onDeleteItem,
  onError
}: DetailseiteProps) => {
  const { register, handleSubmit,
          reset,
          formState: { errors, isValid },
  } = useForm<FormValues>({ mode: 'onChange' });

  useEffect(() => {
    if (errors.productName) {
      onError(errors.productName.message!);
    } else if (errors.quantity){
      onError(errors.quantity.message!)
    } else {
      onError('');
    }
  }, [errors.productName, errors.quantity, onError]);

  function submit(data: FormValues){
    reset({productName: '', quantity: 0})
    onAddItem(data.productName, data.quantity)
  }

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
            <li className={item.status === Status.CLOSED ? 'completed' : ''} key={item.id}>
              <button
                className="check-button"
                type="button"
                aria-label={`${item.name} als erledigt markieren`}
                onClick={() => onToggleItem(item.id)}
              >
                {item.status === Status.CLOSED ? '✓' : ''}
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

        <form className="item-form" onSubmit={handleSubmit(submit)}>
          <label htmlFor="product-name">
            Produktname
            <input
              id="product-name"
              {...register('productName', {
                required: 'Productname is required!',
              })}
            />
          </label>
          <label htmlFor="product-quantity">
            Menge
            <input
              id="product-quantity"
              {...register('quantity', {
                required: 'Quantity is required!',
                min: {
                  value: 1,
                  message: 'Quantity must be greater than 0!',
                },
              })}
            />
          </label>
          <button type="submit" disabled={!isValid}>
            add item
          </button>
        </form>
      </div>
    </section>
  );
}

export default Detailseite
