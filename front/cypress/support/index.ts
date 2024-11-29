import "./commands"

declare global{
  namespace Cypress {
    interface Chainable<Subject = any> {
      login(isAdmin: boolean): typeof this.login
    }
  }
}
