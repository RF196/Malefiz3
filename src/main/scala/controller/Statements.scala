package controller

object Statements extends Enumeration with InstructionInterface:

  type Statements = Value
  val addPlayer, roll, selectFigure, selectField, wrongField, selectWrongFigure, nextPlayer, wall, wrongWall, won, changeFigure = Value

  val value: Handler2 =
    case StatementRequest(controller) if controller.gameboard.statementStatus == roll =>
      s"${controller.gameboard.playerTurn} du bist als erstes dran. Klicke auf den Würfel!"
    case StatementRequest(controller)
      if controller.gameboard.statementStatus ==
        addPlayer =>
      "Spieler wurden erfolgreich angelegt."
    case StatementRequest(controller)
      if controller.gameboard.statementStatus ==
        wrongField =>
      "Nicht so schnell! Gehe bitte nur auf die markierten Felder!"
    case StatementRequest(controller)
      if controller.gameboard.statementStatus ==
        selectFigure =>
      s"${controller.gameboard.playerTurn} du hast eine ${controller.gameboard.dice} gewürfelt. " +
        s"Wähle deine gewünschte Figur aus!"
    case StatementRequest(controller)
      if controller.gameboard.statementStatus ==
        selectField =>
      "Du kannst nun auf folgende Felder gehen. Wähle ein markiertes Felder aus."
    case StatementRequest(controller)
      if controller.gameboard.statementStatus ==
        selectWrongFigure =>
      s"${controller.gameboard.playerTurn} bitte wähle deine eigene Figur aus!"
    case StatementRequest(controller)
      if controller.gameboard.statementStatus ==
        nextPlayer =>
      s"${controller.gameboard.playerTurn} du bist als nächstes dran. Klicke auf den Würfel!"
    case StatementRequest(controller)
      if controller.gameboard.statementStatus ==
        wall =>
      s"${controller.gameboard.playerTurn} du bist auf eine Mauer gekommen. Lege Sie bitte um."
    case StatementRequest(controller)
      if controller.gameboard.statementStatus ==
        wrongWall =>
      s"${controller.gameboard.playerTurn} du darfst die Mauer dort nicht setzen. Bitte wähle ein anderes Feld " +
        s"aus."
    case StatementRequest(controller)
      if controller.gameboard.statementStatus ==
        won =>
      s"Gratulation ${controller.gameboard.playerTurn} du hast das Spiel gewonnen! "
    case StatementRequest(controller)
      if controller.gameboard.statementStatus ==
        changeFigure =>
      s"${controller.gameboard.playerTurn} du hast deine Figur abgewählt. Wähle bitte erneut eine Figur aus!"