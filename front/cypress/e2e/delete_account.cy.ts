describe('Delete account spec', () => {
  beforeEach(() => {
    cy.login();

    cy.intercept('GET', '/api/user/1', {
      statusCode: 200,
      body: {
        id: 1,
        firstName: 'John',
        lastName: 'Doe',
        email: 'john.doe@example.com',
        admin: false,
        createdAt: '2023-01-01T00:00:00Z',
        updatedAt: '2024-01-01T00:00:00Z',
      },
    }).as('getUserInfo');

    cy.intercept('DELETE', '/api/user/1', {
      statusCode: 200,
    }).as('deleteUser');
  })

  it('Deleted account successfull', () => {
    cy.contains('Account').click();

    cy.wait('@getUserInfo');

    cy.get('#deleteButton').should('be.visible');

    cy.get('#deleteButton').click();

    cy.wait('@deleteUser');

    cy.url().should('include', '/');

    cy.contains('Your account has been deleted !').should('be.visible');
  });
})
