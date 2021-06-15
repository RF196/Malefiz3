package controller.instructions

import controller.{InstructionInterface, Request, StatementRequest, Statements}
import controller.Statements._
import controller.gamestates.SetFigure

object ISelectFigure extends InstructionInterface:

  val select1: Handler0 = {
    case Request(inputList, gamestate, controller)
      if inputList.head.toInt == controller.gameboard.playerTurn.get.number =>
        controller.calculatePath(
          controller.getPlayerFigureLocation(inputList.head.toInt, inputList(1).toInt),
          controller.gameboard.dice.get
        )
      Request(inputList, gamestate, controller)
  }

  val select2: Handler0 = {
    case Request(inputList, gamestate, controller) =>
      controller.setSelectedFigure(controller.gameboard.playerTurn.get, 
        inputList(0).toInt)
      Request(inputList, gamestate, controller)
  }

  val select3: Handler0 = {
    case Request(inputList, gamestate, controller) =>
      controller.setPossibleCellsTrueOrFalse(controller.gameboard.possibleCells.toList)
      Request(inputList, gamestate, controller)
  }

  val select4: Handler1 = {
    case Request(inputList, gameState, controller) =>
      gameState.nextState(SetFigure(controller))
      controller.setStatementStatus(selectField)
      Statements.value(StatementRequest(controller))
  }


  val select5: Handler0 = {
    case Request(inputList, gameState, controller)
      if inputList.head.toInt != controller.gameboard.playerTurn.get.number =>
      Request(inputList, gameState, controller)
  }

  val select6: Handler1 = {
    case Request(inputList, gameState, controller) =>
      controller.setStatementStatus(selectWrongFigure)
      Statements.value(StatementRequest(controller))
  }

  val select: PartialFunction[Request, String] = (select1
    .andThen(select2)
    .andThen(select3)
    .andThen(select4)
    .andThen(log))
    .orElse(select5.andThen(select6).andThen(log))
