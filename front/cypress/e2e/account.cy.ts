describe('Account session spec', () => {
  beforeEach(() => {

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

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []
    ).as('getSessions');

    cy.login();
  })

  it('Account session successfull', () => {
    cy.contains('Account').click()

    cy.wait('@getUserInfo')

    cy.contains('Name: John DOE').should('be.visible');
    cy.contains('Email: john.doe@example.com').should('be.visible');
    cy.contains('Create at: January 1, 2023').should('be.visible');
    cy.contains('Last update: January 1, 2024').should('be.visible');
  });
})
