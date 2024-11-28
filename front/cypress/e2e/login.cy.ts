describe('Login spec', () => {
    beforeEach(() => {
      cy.intercept('POST', '/api/auth/login', {
        body: {
          id: 1,
          username: 'userName',
          firstName: 'firstName',
          lastName: 'lastName',
          admin: true
        },
      }).as('postLogin')

      cy.intercept(
        {
          method: 'GET',
          url: '/api/session',
        },
        []).as('getSessions')

      cy.visit('/')
    })

  it('Login successfull', () => {
    cy.contains('Login').click()

    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

    cy.wait('@postLogin')

    cy.wait('@getSessions')

    cy.url().should('include', '/sessions')
  })
});
