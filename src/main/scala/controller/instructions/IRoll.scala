package controller.instructions

import controller.{InstructionInterface, Request, StatementRequest, Statements}
import controller.Statements._
import controller.gamestates.SelectFigure

object IRoll extends InstructionInterface:

  val roll1: Handler0 = {
    case Request(inputList, gameState, controller) if inputList.head != " " =>
      controller.setDicedNumber(controller.rollCube)
      Request(inputList, gameState, controller)
  }

  val roll2: Handler0 = {
    case Request(inputList, gameState, controller) =>
      controller.setPossibleFiguresTrueOrFalse(controller.gameboard.playerTurn.get.number)
      Request(inputList, gameState, controller)
  }

  val roll3: Handler1 = {
    case Request(inputList, gameState, controller) =>
      gameState.nextState(SelectFigure(controller))
      controller.setStatementStatus(selectFigure)
      Statements.value(StatementRequest(controller))
  }

  val roll: PartialFunction[Request, String] = roll1.andThen(roll2).andThen(roll3).andThen(log)

