describe('Logout spec', () => {
  beforeEach(() => {
    cy.login();
  })

  it('Logout successfull', () => {
    cy.contains('Account').should('be.visible');

    cy.contains('Logout').click();

    cy.url().should('include', '/');

    cy.contains('Login').should('be.visible');
    cy.contains('Register').should('be.visible');
  });
})
