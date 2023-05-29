Feature: User Management

  Scenario: client register new user 

  Scenario: register new user and send email
  	Given the test user 'reguser1' not exists
    When the client register new user 'reguser1'
    Then the client should be able to query the user 'reguser1'
    Then the client should be able to see the registration event