describe('Detail session spec', () => {
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
          "date": "2024-03-22T00:00:00.000+00:00",
          "teacher_id": 1,
          "description": "Test desc",
          "users": [],
          "createdAt": "2024-11-27T20:09:34",
          "updatedAt": "2024-11-27T20:09:34"
        }
      ]
    ).as('getSessions');

    cy.intercept(
      {
        method: 'GET',
        url: '/api/teacher/1',
      },
      {
        "id": 1,
        "lastName": "DELAHAYE",
        "firstName": "Margot",
        "createdAt": "2024-11-15T11:05:38",
        "updatedAt": "2024-11-15T11:05:38"
      }
    ).as('getSession');

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session/1',
      },
      {
        "id": 1,
        "name": "Cours gratuit",
        "date": "2024-03-22T00:00:00.000+00:00",
        "teacher_id": 1,
        "description": "Test desc",
        "users": [],
        "createdAt": "2024-11-27T20:09:34",
        "updatedAt": "2024-11-27T20:09:34"
      }
    ).as('getSession');

    cy.login();
  })

  it('Detail session successfull', () => {
    cy.wait('@getSessions')

    cy.contains("Detail").click()

    cy.wait('@getSession')

    cy.contains('Cours Gratuit').should('be.visible');
  });
})
