import "../support/index"

describe('Create session spec', () => {
  beforeEach(() => {
    cy.login(true);

    cy.intercept('GET', '/api/teacher', {
      statusCode: 200,
      body: [
        { id: '1', firstName: 'John', lastName: 'DOE' },
        { id: '2', firstName: 'Jane', lastName: 'SMITH' },
      ],
    }).as('getTeachers');

    cy.intercept('POST', '/api/session', {
      statusCode: 201,
      body: {}
    }).as('createSession')
  })

  it('Create session successfull', () => {
    cy.contains('Create').click()

    cy.wait('@getTeachers');

    cy.get('input[formControlName="name"]').type('Morning Yoga');
    cy.get('input[formControlName="date"]').type('2024-12-01');
    cy.get('mat-select[formControlName="teacher_id"]').click();
    cy.get('mat-option').contains('John DOE').click();
    cy.get('textarea[formControlName="description"]').type('A relaxing yoga session to start your day.');

    cy.get('button[type="submit"]').click();

    cy.wait('@createSession')

    cy.contains('Session created !').should('be.visible');
  });
})
