describe('Account spec', () => {
  it('Account displayed', () => {
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

    cy.intercept('GET', '/api/user/1', {
      body: {
        id: 1,
        email: 'user@studio.com',
        password: 'password',
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

    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

    cy.url().should('include', '/sessions')

    cy.get('.link').contains("Account").click()
    cy.get('h1').contains("User information")

  })

})

