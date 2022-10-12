describe('Register spec', () => {
  it('Register a new user', () => {
    cy.visit('/register')

    cy.intercept(
      {
        method: 'POST',
        url: '/api/auth/register'
      },
      []).as('register')

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'testUserName',
        firstName: 'testFirstName',
        lastName: 'testLastName',
        admin: true
      },
    })

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []).as('session')

    cy.get('input[formControlName=firstName]').type("bob")
    cy.get('input[formControlName=lastName]').type("obo")
    cy.get('input[formControlName=email]').type("bob@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}`)

    cy.url().should('include', '/login')

    cy.get('input[formControlName=email]').type("bob@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
    cy.url().should('include', '/sessions')
  })
  
  it('Fail registration', () => {
    cy.visit('/register')

    cy.get('input[formControlName=firstName]').type("bob")
    cy.get('input[formControlName=lastName]').type("obo")
    cy.get('input[formControlName=email]').type("bob@studio.com")
    cy.get('input[formControlName=password]').type(`${"test"}{enter}`)

    cy.get('.error').should('contain', 'An error occurred')

  })
})

