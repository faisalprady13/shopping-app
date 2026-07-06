import type { FormEvent } from 'react'
import { PageHeader } from './Header'

type StartseiteProps = {
  username: string
  onUsernameChange: (username: string) => void
  onLogin: (event: FormEvent<HTMLFormElement>) => void
}

const Startseite = ({ username, onUsernameChange, onLogin }: StartseiteProps) => {
  return (
    <section className="wire-panel start-panel" aria-labelledby="start-title">
      <PageHeader title="Startseite" subtitle="Gib deinen Namen ein und starte deine Listen." />

      <form className="name-form" onSubmit={onLogin}>
        <label htmlFor="username">Name</label>
        <div className="form-row">
          <input
            id="username"
            value={username}
            onChange={(event) => onUsernameChange(event.target.value)}
            autoComplete="name"
          />
          <button type="submit">Start</button>
        </div>
      </form>
    </section>
  )
}

export default Startseite
