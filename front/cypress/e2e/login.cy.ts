import "../support/index"

describe('Login spec', () => {
    beforeEach(() => {
      cy.login(true)
    })

  it('Login successfull', () => {

    cy.url().should('include', '/sessions')
  })
});
