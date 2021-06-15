package controller.instructions

import controller.{InstructionInterface, Request, StatementRequest, Statements}
import controller.Statements._
import controller.gamestates.Roll
import model.baseImpl.Figure

object ISetWall extends InstructionInterface {

  val set1: Handler0 = {
    case Request(inputList, gameState, controller)
      if controller.gameboard.cells(inputList.head.toInt).wallPermission => 
        controller.gameboard.cells(inputList.head.toInt).contains match {
          case figure: Figure => Request(inputList, gameState, controller)
          case string: String =>
            if (string == "EMPTY") {
              controller.placeOrRemoveWall(inputList.head.toInt, true)
              Request(inputList, gameState, controller)
            } else {
              Request(inputList, gameState, controller)
            }
        }
  }

  val set2: Handler0 = {
    case Request(inputList, gameState, controller) => 
      if (!controller.gameboard.cells(inputList.head.toInt).wallPermission) {
        Request(inputList, gameState, controller)
      }

      controller.gameboard.cells(inputList.head.toInt).contains match {
        case figure: Figure =>
          if (figure.number != 0) {
            Request(inputList, gameState, controller)
          }
        case string: String =>
          if (string == "WALL") {
            Request(inputList, gameState, controller)
          }
      }
      Request(inputList, gameState, controller)
  }

  val set3: Handler1 = {
    case Request(inputList, gameState, controller) =>
      controller.setDicedNumber(Some(0))
      controller.setPossibleFiguresTrueOrFalse(controller.gameboard.playerTurn.get.number)
      controller.setPossibleCellsTrueOrFalse(controller.gameboard.possibleCells.toList)
      controller.setPlayersTurn(
        controller.nextPlayer(controller.gameboard.players, controller.gameboard.playerTurn.get.number - 1)
      )
      gameState.nextState(Roll(controller))
      controller.setStatementStatus(nextPlayer)
      Statements.value(StatementRequest(controller))
  }

  val set4: Handler1 = {
    case Request(inputList, gameState, controller) =>
      controller.setStatementStatus(wrongWall)
      Statements.value(StatementRequest(controller))
  }

  val set: PartialFunction[Request, String] = (set1.andThen(set3).andThen(log)).orElse(set2.andThen(set4).andThen(log))

}
