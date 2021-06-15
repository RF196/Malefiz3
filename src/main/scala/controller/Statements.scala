package controller

object Statements extends Enumeration with InstructionInterface:

  type Statements = Value
  val addPlayer, roll, selectFigure, selectField, wrongField, selectWrongFigure, nextPlayer, wall, wrongWall, won, changeFigure = Value

  val value: Handler2 =
    
    case StatementRequest(controller) 
      if controller.gameboard.statementStatus.get == roll =>
        s"${controller.gameboard.playerTurn.get} du bist als erstes dran. Klicke auf den Würfel!"
  
    case StatementRequest(controller)
      if controller.gameboard.statementStatus.get == addPlayer =>
        "Spieler wurden erfolgreich angelegt."
  
    case StatementRequest(controller)
      if controller.gameboard.statementStatus.get == wrongField =>
        "Nicht so schnell! Gehe bitte nur auf die markierten Felder!"
  
    case StatementRequest(controller)
      if controller.gameboard.statementStatus.get == selectFigure =>
        s"${controller.gameboard.playerTurn.get} du hast eine ${controller.gameboard.dice.get} gewürfelt. " +
        s"Wähle deine gewünschte Figur aus!"
  
    case StatementRequest(controller)
      if controller.gameboard.statementStatus.get == selectField =>
        "Du kannst nun auf folgende Felder gehen. Wähle ein markiertes Felder aus."
  
    case StatementRequest(controller)
      if controller.gameboard.statementStatus.get == selectWrongFigure =>
        s"${controller.gameboard.playerTurn.get} bitte wähle deine eigene Figur aus!"
  
    case StatementRequest(controller)
      if controller.gameboard.statementStatus.get == nextPlayer =>
        s"${controller.gameboard.playerTurn.get} du bist als nächstes dran. Klicke auf den Würfel!"
  
    case StatementRequest(controller)
      if controller.gameboard.statementStatus.get == wall =>
        s"${controller.gameboard.playerTurn.get} du bist auf eine Mauer gekommen. Lege Sie bitte um."
  
    case StatementRequest(controller)
      if controller.gameboard.statementStatus.get == wrongWall =>
        s"${controller.gameboard.playerTurn.get} du darfst die Mauer dort nicht setzen. Bitte wähle ein anderes Feld aus."
  
    case StatementRequest(controller)
      if controller.gameboard.statementStatus.get == won =>
        s"Gratulation ${controller.gameboard.playerTurn.get} du hast das Spiel gewonnen! "
  
    case StatementRequest(controller)
      if controller.gameboard.statementStatus.get == changeFigure =>
        s"${controller.gameboard.playerTurn.get} du hast deine Figur abgewählt. Wähle bitte erneut eine Figur aus!"