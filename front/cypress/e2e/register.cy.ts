describe('Register spec', () => {
  beforeEach(() => {
    cy.intercept('POST', '/api/auth/register', {
      statusCode: 201,
      body: {}
    }).as('registerUser');
  });


  it('Register successfull', () => {

    cy.visit('/register');

    cy.get('input[formControlName="firstName"]').type('John');
    cy.get('input[formControlName="lastName"]').type('Doe');
    cy.get('input[formControlName="email"]').type('john.doe@example.com');
    cy.get('input[formControlName="password"]').type('password123');

    cy.get('button[type="submit"]').click();

    cy.wait('@registerUser');

    cy.url().should('include', '/login');
  })
})
