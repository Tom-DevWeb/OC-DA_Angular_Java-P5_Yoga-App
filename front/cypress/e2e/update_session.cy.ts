import "../support/index"

describe('Update session spec', () => {
  beforeEach(() => {
    cy.intercept(
      {
        method: 'PUT',
        url: '/api/session/1',
      },
      {
        "id": 1,
        "name": "Nouvelle session",
        "date": "2024-03-22T00:00:00.000+00:00",
        "teacher_id": 1,
        "description": "Test desc",
        "users": [],
        "createdAt": "2024-11-27T20:09:34",
        "updatedAt": "2024-11-27T20:09:34"
      }
    ).as('updateSession');

    cy.intercept(
      {
        method: 'GET',
        url: '/api/teacher',
      },
      {
        "id": 1,
        "lastName": "DELAHAYE",
        "firstName": "Margot",
        "createdAt": "2024-11-15T11:05:38",
        "updatedAt": "2024-11-15T11:05:38"
      }
    ).as('getTeacher');

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
        }
      ]
    ).as('getSessions');

    cy.login(true);
  })

  it('Update session successfull', () => {
    cy.wait('@getSessions')

    cy.contains("Edit").click()

    cy.wait('@getSession')

    cy.wait('@getTeacher')

    cy.get('input[formControlName="name"]').type('{selectall}{backspace}Nouvelle session');

    cy.get('button[type="submit"]').click();

    cy.wait('@updateSession')

    cy.contains('Session updated !').should('be.visible');

    cy.url().should('include', '/sessions');
  });
})
