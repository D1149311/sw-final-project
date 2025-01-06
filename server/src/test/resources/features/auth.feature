Feature: Login and register the game
  Auth the website

  Scenario: Login Successfully
    Given Client0 entered the website
    Then Client0 should be in the "login" page
    When Client0 enter "abc" in the username field
    And Client0 enter "def" in the password field
    And Client0 click the "login" button
    Then Client0 should be in the "" page
    Then Client0 should see the greeting "歡迎，abc"

    Given Client1 entered the website
    Then Client1 should be in the "login" page
    When Client1 enter "abcd" in the username field
    And Client1 enter "abcd" in the password field
    And Client1 click the "login" button
    Then Client1 should be in the "" page
    Then Client1 should see the greeting "歡迎，abcd"

  Scenario: Login with unknown account
    Given Client0 entered the website
    Then Client0 should be in the "login" page
    When Client0 enter "123" in the username field
    And Client0 enter "123" in the password field
    And Client0 click the "login" button
    Then Client0 should be in the "login" page

  Scenario: Login with wrong password
    Given Client1 entered the website
    Then Client1 should be in the "login" page
    When Client1 enter "abc" in the username field
    And Client1 enter "abcd" in the password field
    And Client1 click the "login" button
    Then Client1 should be in the "login" page
