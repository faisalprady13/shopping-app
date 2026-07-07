type AddListSeiteProps = {
  listName: string,
  onListNameChange: (listName: string) => void,
  submitList: (listName: string) => void
}

const AddListSeite=({
  listName,
  onListNameChange,
  submitList
}:AddListSeiteProps) => {
  return (
    <section className="wire-panel details-panel" aria-labelledby="adds-title">
      <header className="page-title adds-title">
        <h1 id="adds-title">Neue Liste Seite</h1>
        <p>Fügen Sie einen Namen hinzu.</p>
      </header>
      <form className="list-form" onSubmit={submitList}>
        <label htmlFor="list-name">Listenname</label>
        <input
          id="list-name"
          value={listName}
          onChange={(event) => onListNameChange(event.target.value)}
        />
        <button type="submit">add Shopping List</button>
      </form>
    </section>
  )
}

export default AddListSeite