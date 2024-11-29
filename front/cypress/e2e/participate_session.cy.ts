import "../support/index"

describe('Participate session spec', () => {
  beforeEach(() => {

    cy.intercept('DELETE', '/api/session/1/participate/1', {}
    ).as('unParticipateSession');

    cy.intercept('POST', '/api/session/1/participate/1', {}
    ).as('participateSession');

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

    cy.login(false);
  })

  it('Participate session successfull', () => {
    cy.wait('@getSessions')

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

    cy.contains("Detail").click()

    cy.wait('@getSession')

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
        "users": [1],
        "createdAt": "2024-11-27T20:09:34",
        "updatedAt": "2024-11-27T20:09:34"
      }
    ).as('getSessionWithParticipation');

    cy.contains("Participate").click()

    cy.wait('@participateSession')

    cy.wait('@getSessionWithParticipation')

    cy.contains('Do not participate').should('be.visible')
  });

  it('Not participate to session', () => {
    cy.wait('@getSessions')

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
        "users": [1],
        "createdAt": "2024-11-27T20:09:34",
        "updatedAt": "2024-11-27T20:09:34"
      }
    ).as('getSession');

    cy.contains("Detail").click()

    cy.wait('@getSession')

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
        "users": [0],
        "createdAt": "2024-11-27T20:09:34",
        "updatedAt": "2024-11-27T20:09:34"
      }
    ).as('getSessionWithParticipation');

    cy.contains("Do not participate").click()

    cy.wait('@unParticipateSession')

    cy.wait('@getSessionWithParticipation')

    cy.contains('Participate').should('be.visible')
  });
})
