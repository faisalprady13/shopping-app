import { useForm } from 'react-hook-form';

type FormValues = {
  shoppingListName: string
}

type AddListSeiteProps = {
  listName: string,
  onBack: () => void,
  submitList: (listName: string) => void
};

const AddListSeite=({
  listName,
  onBack,
  submitList
}:AddListSeiteProps) => {
  const { register, handleSubmit, formState: {  isValid }, } =
    useForm<FormValues>({ mode: 'onChange' });

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
          <label htmlFor="list-name">Listenname</label>
          <input
            id="list-name"
            {...register('shoppingListName', {
              required: 'Name is required!',
            })}
          />
          <button type="submit" disabled={!isValid}>
            add Shopping List
          </button>
        </form>
      </div>
    </section>
  );
}

export default AddListSeite