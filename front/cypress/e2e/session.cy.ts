describe('Session spec', () => {

  beforeEach(() => {

    cy.visit('/login')

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'testUserName',
        firstName: 'testFirstName',
        lastName: 'testLastName',
        admin: true
      },
    })

    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

    cy.intercept('GET', '/api/session', {
      body: [{
        id: 1,
        name: "session1",
        description: "desc",
        date: "2025-11-01",
        teacher_id: 1
      }],
    })

    cy.url().should('include', '/sessions')

  })

  it('List session displayed', () => {

    cy.get(".ml1").should('be.visible')
    cy.get('item').should('not.be.empty')

  })

  it('Session details displayed', () => {

    cy.intercept('GET', '/api/session/1', {
      body: {
        id: 1,
        name: "session1",
        description: "desc",
        date: "2025-11-01",
        teacher_id: 1
      },
    })

    cy.get('button[testid=detail]').first().click()

    cy.get("h1").contains("Session1")
    cy.contains("Delete").should('be.visible')

  })

  it('Session creation', () => {

    cy.intercept('GET', '/api/teacher', {
      body: [{
        id: 1,
        lastName: "DELAHAYE",
        firstName: "Margot"
      }],
    })

    cy.get('button[routerLink=create]').click()

    cy.get('input[formControlName=name]').type("session_cyp")
    cy.get('input[formControlName=date]').type("2025-11-01")

    cy.get('mat-select[formcontrolname="teacher_id"]').click();
    cy.get('mat-option').contains('Margot DELAHAYE').click();

    cy.get('textarea[formControlName="description"]').type(`${"test"}`)
    cy.get('button[type=submit]').click()
  })

  it('Session edit', () => {

    cy.intercept('GET', '/api/session/1', {
      body: {
        id: 1,
        name: "session1",
        description: "desc",
        date: "2025-11-01",
        teacher_id: 1
      },
    })

    cy.intercept('GET', '/api/teacher', {
      body: [{
        id: 1,
        lastName: "DELAHAYE",
        firstName: "Margot"
      }],
    })

    cy.intercept('POST', '/api/session/1', {
      body: {
        id: 1,
        name: "session1",
        description: "desc",
        date: "2025-11-01",
        teacher_id: 1
      },
    })

    cy.get('button[testid=edit]').click()

    cy.get('input[formControlName=name]').type("session_cyp_updated")
    cy.get('button[type=submit]').click()
  })

  it('Session delete', () => {

    cy.intercept('GET', '/api/session/1', {
      body: {
        id: 1,
        name: "session1",
        description: "desc",
        date: "2025-11-01",
        teacher_id: 1
      },
    })

    cy.get('button[testid=detail]').first().click()

    cy.contains("Delete").should('be.visible')

    cy.get('button[testid=delete]').click()


  })

})

