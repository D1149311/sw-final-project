Feature: Play Online
  Play the chess with random player

  Scenario: Checkmate
    Given Client0 entered the website
    When Client0 enter "abc" in the username field
    And Client0 enter "def" in the password field
    And Client0 click the "login" button
    Then Client0 should be in the "" page

    Given Client1 entered the website
    When Client1 enter "abcd" in the username field
    And Client1 enter "abcd" in the password field
    And Client1 click the "login" button
    Then Client1 should be in the "" page

    When Client0 click the "create" button
    Then Client0 should see the waiting text
    And Client0 save the room number
    When Client1 click the "friend" button
    And Client1 enter the room number with client0
    And Client1 accept the alert
    Then Client0 should be in the "game" page
    And Client1 should be in the "game" page

    # white move
    When Client1 click the board at 3 6
    And Client1 click the board at 3 4
    Then Client0 should see the "wp" on the grid 3 4
    Then Client1 should see the "wp" on the grid 3 4

    # not your turn
    When Client1 click the board at 3 4
    And Client1 click the board at 3 3
    Then Client0 should see the "wp" on the grid 3 4
    Then Client1 should see the "wp" on the grid 3 4

    # black move
    When Client0 click the board at 4 1
    And Client0 click the board at 4 3
    Then Client0 should see the "bp" on the grid 4 3
    Then Client1 should see the "bp" on the grid 4 3

    # white eat
    When Client1 click the board at 3 4
    And Client1 click the board at 4 3
    Then Client0 should see the "wp" on the grid 4 3
    Then Client1 should see the "wp" on the grid 4 3

    # black move
    When Client0 click the board at 3 1
    And Client0 click the board at 3 3
    Then Client0 should see the "bp" on the grid 3 3
    Then Client1 should see the "bp" on the grid 3 3

    # white 過路兵
    When Client1 click the board at 4 3
    And Client1 click the board at 3 2
    Then Client0 should see the "wp" on the grid 3 2
    Then Client1 should see the "wp" on the grid 3 2
    Then Client0 should see the "" on the grid 3 3
    Then Client1 should see the "" on the grid 3 3

    # black move
    When Client0 click the board at 3 0
    And Client0 click the board at 7 4
    Then Client0 should see the "bq" on the grid 7 4
    Then Client1 should see the "bq" on the grid 7 4

    # white move
    When Client1 click the board at 6 6
    And Client1 click the board at 6 4
    Then Client0 should see the "wp" on the grid 6 4
    Then Client1 should see the "wp" on the grid 6 4

    # black move
    When Client0 click the board at 5 0
    And Client0 click the board at 4 1
    Then Client0 should see the "bb" on the grid 4 1
    Then Client1 should see the "bb" on the grid 4 1

    # white move
    When Client1 click the board at 6 4
    And Client1 click the board at 6 3
    Then Client0 should see the "wp" on the grid 6 3
    Then Client1 should see the "wp" on the grid 6 3

    # black move
    When Client0 click the board at 4 0
    And Client0 click the board at 5 0
    Then Client0 should see the "bk" on the grid 5 0
    Then Client1 should see the "bk" on the grid 5 0

    # white move
    When Client1 click the board at 3 2
    And Client1 click the board at 3 1
    Then Client0 should see the "wp" on the grid 3 1
    Then Client1 should see the "wp" on the grid 3 1

    # black move
    When Client0 click the board at 4 1
    And Client0 click the board at 6 3
    Then Client0 should see the "bb" on the grid 6 3
    Then Client1 should see the "bb" on the grid 6 3

    # white move
    When Client1 click the board at 1 6
    And Client1 click the board at 1 4
    Then Client0 should see the "wp" on the grid 1 4
    Then Client1 should see the "wp" on the grid 1 4

    # black move
    When Client0 click the board at 6 3
    And Client0 click the board at 5 4
    Then Client0 should see the "bb" on the grid 5 4
    Then Client1 should see the "bb" on the grid 5 4

    # white move
    When Client1 click the board at 0 6
    And Client1 click the board at 0 5
    Then Client0 should see the "wp" on the grid 0 5
    Then Client1 should see the "wp" on the grid 0 5

    # black move
    When Client0 click the board at 7 4
    And Client0 click the board at 6 5
    Then Client0 should see the "bq" on the grid 6 5
    Then Client1 should see the "bq" on the grid 6 5

    # white move
    When Client1 click the board at 3 1
    And Client1 click the board at 3 0
    Then Client0 should see the "wp" on the grid 3 0
    Then Client1 should see the "wp" on the grid 3 0

    # 升變
    When Client1 choose "QUEEN" to promote

    And Client1 should see the "BLACK is checkmated!" on the alert
    And Client1 accept the alert
    And Client0 should see the "BLACK is checkmated!" on the alert
    And Client0 accept the alert
    And Client0 should be in the "" page
    And Client1 should be in the "" page
