type HeaderProps = {
  username: string
  isLoggedIn: boolean
  onLogout: () => void
}

export const PageHeader = ({ title, subtitle }: { title: string; subtitle?: string }) => {
  return (
    <header className="page-title">
      <h1>{title}</h1>
      {subtitle ? <p>{subtitle}</p> : null}
    </header>
  )
}

const Header = ({ username, isLoggedIn, onLogout }: HeaderProps) => {
  const status = isLoggedIn ? 'login' : 'logout'
  const displayedUsername = isLoggedIn ? username : 'Guest'

  return (
    <header className="app-header">
      <strong className="app-name">shopping-app</strong>

      <div className="app-user-info">
        <span>Username: {displayedUsername}</span>
        <span>Status: {status}</span>
      </div>

      {isLoggedIn ? (
        <button type="button" onClick={onLogout}>
          logout
        </button>
      ) : null}
    </header>
  )
}

export default Header
