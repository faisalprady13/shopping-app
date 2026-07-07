import { useForm } from 'react-hook-form';
import { useEffect } from 'react';

type FormValues = {
  shoppingListName: string
}

type AddListSeiteProps = {
  listName: string,
  onBack: () => void,
  submitList: (listName: string) => void
  onError: (message: string) => void
};

const AddListSeite=({
  listName,
  onBack,
  submitList,
  onError
}:AddListSeiteProps) => {
  const { register, handleSubmit, formState: { errors, isValid }, } =
    useForm<FormValues>({ mode: 'onChange' });

  useEffect(() => {
    if(errors.shoppingListName){
      onError(errors.shoppingListName.message!)
    } else {
      onError("")
    }
  }, [errors.shoppingListName, onError]);

  function submit(data: FormValues){
    submitList(data.shoppingListName);
  }

  return (
    <section className="wire-panel details-panel" aria-labelledby="adds-title">
      <header className="page-title adds-title">
        <h1 id="adds-title">Neue Liste Seite</h1>
        <p>Fügen Sie einen Namen hinzu.</p>
      </header>
      <div className="shopping-list-row">
        <strong>{listName}</strong>
        <button type="button" onClick={onBack}>
          back
        </button>
      </div>
      <div className="add-content">
        <form className="list-form" onSubmit={handleSubmit(submit)}>
          <label htmlFor="shoppingListName">
            Listenname
            <input
              id="shoppingListName"
              {...register('shoppingListName', {
                required: 'Name is required!',
                minLength: {
                  value: 5,
                  message: "Name must be at least 5 characters long."}
              })}
            />
          </label>
          <button type="submit" disabled={!isValid}>
            add Shopping List
          </button>
        </form>
      </div>
    </section>
  );
}

export default AddListSeite