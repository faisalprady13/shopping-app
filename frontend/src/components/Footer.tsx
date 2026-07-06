type FooterProps = {
  errormessage: string
}

const Footer = ({errormessage}:FooterProps) => {
  return (
    <footer className="app-footer">
      <div>shopping-app © All rights reserved.</div>
      <div id="errmes">{errormessage}</div>
    </footer>
  );
}

export default Footer
