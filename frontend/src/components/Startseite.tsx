import type { FormEvent } from 'react'
import { useState } from 'react'
import { PageHeader } from './Header'
import type { LoginDto, RegisterDto } from '../types'

type StartseiteProps = {
  onLogin: (loginDto: LoginDto) => void
  onRegister: (registerDto: RegisterDto) => void
}

const Startseite = ({ onLogin, onRegister }: StartseiteProps) => {
  const [registerName, setRegisterName] = useState('')
  const [registerEmail, setRegisterEmail] = useState('')
  const [registerPassword, setRegisterPassword] = useState('')
  const [loginEmail, setLoginEmail] = useState('')
  const [loginPassword, setLoginPassword] = useState('')

  const handleRegister = (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault()

    onRegister({
      name: registerName.trim(),
      email: registerEmail.trim(),
      password: registerPassword,
    })
  }

  const handleLogin = (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault()

    onLogin({
      email: loginEmail.trim(),
      password: loginPassword,
    })
  }

  return (
    <section className="wire-panel start-panel" aria-labelledby="start-title">
      <PageHeader title="Startseite" subtitle="Melde dich an oder erstelle ein Konto fuer deine Listen." />

      <div className="auth-layout">
        <form className="auth-form" onSubmit={handleLogin}>
          <h2>Login</h2>

          <label htmlFor="login-email">E-Mail</label>
          <input
            id="login-email"
            type="email"
            value={loginEmail}
            onChange={(event) => setLoginEmail(event.target.value)}
            autoComplete="email"
            required
          />

          <label htmlFor="login-password">Passwort</label>
          <input
            id="login-password"
            type="password"
            value={loginPassword}
            onChange={(event) => setLoginPassword(event.target.value)}
            autoComplete="current-password"
            required
          />

          <button type="submit">Login</button>
        </form>

        <form className="auth-form" onSubmit={handleRegister}>
          <h2>Registrieren</h2>

          <label htmlFor="register-name">Name</label>
          <input
            id="register-name"
            value={registerName}
            onChange={(event) => setRegisterName(event.target.value)}
            autoComplete="name"
            required
          />

          <label htmlFor="register-email">E-Mail</label>
          <input
            id="register-email"
            type="email"
            value={registerEmail}
            onChange={(event) => setRegisterEmail(event.target.value)}
            autoComplete="email"
            required
          />

          <label htmlFor="register-password">Passwort</label>
          <input
            id="register-password"
            type="password"
            value={registerPassword}
            onChange={(event) => setRegisterPassword(event.target.value)}
            autoComplete="new-password"
            minLength={8}
            required
          />

          <button type="submit">Konto erstellen</button>
        </form>

        <div className="oauth-actions" aria-label="OAuth Login">
          <a className="oauth-button" href="http://localhost:8080/oauth2/authorization/google">Google</a>
          <a className="oauth-button" href="http://localhost:8080/oauth2/authorization/github">GitHub</a>
        </div>
      </div>
    </section>
  )
}

export default Startseite
