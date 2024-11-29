import "../support/index"

describe('List session spec', () => {
  beforeEach(() => {

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      [
        {
          "id": 1,
          "name": "Cours gratuit",
          "date": "2024-12-22T00:00:00.000+00:00",
          "teacher_id": 1,
          "description": "Test desc1",
          "users": [],
          "createdAt": "2024-11-27T20:09:34",
          "updatedAt": "2024-11-27T20:09:34"
        },
        {
          "id": 2,
          "name": "Cours débutant",
          "date": "2024-12-22T00:00:00.000+00:00",
          "teacher_id": 1,
          "description": "Test desc2",
          "users": [],
          "createdAt": "2024-11-27T20:09:34",
          "updatedAt": "2024-11-27T20:09:34"
        },
        {
          "id": 3,
          "name": "Cours expert",
          "date": "2024-12-22T00:00:00.000+00:00",
          "teacher_id": 1,
          "description": "Test desc3",
          "users": [],
          "createdAt": "2024-11-27T20:09:34",
          "updatedAt": "2024-11-27T20:09:34"
        }
      ]
    ).as('getSessions');

    cy.login(true);
  })

  it('List session successfull', () => {
    cy.wait('@getSessions')

    cy.contains('Cours gratuit').should('be.visible');
    cy.contains('Cours débutant').should('be.visible');
    cy.contains('Cours expert').should('be.visible');
  });
})
