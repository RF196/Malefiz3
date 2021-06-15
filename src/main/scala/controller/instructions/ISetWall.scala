package controller.instructions

import controller.{InstructionInterface, Request, StatementRequest, Statements}
import controller.Statements._
import controller.gamestates.Roll
import model.baseImpl.Figure

object ISetWall extends InstructionInterface {

  val set1: Handler0 = {
    case Request(inputList, gamestate, controller) =>
      controller.gameboard.cells(inputList.head.toInt).contains match {
        case figure: Figure =>
        case string: String =>
          if (string == "EMPTY") {
            controller.placeOrRemoveWall(inputList.head.toInt, true)
          }
      }
      Request(inputList, gamestate, controller)
  }

  
  // Auf der Cell ist eine Mauer oder ein Spieler  => Fehlerfall
  val set2: Handler0 = {
    case Request(inputList, gamestate, controller) =>
      controller.gameboard.cells(inputList.head.toInt).contains match {
        case figure: Figure => Request(inputList, gamestate, controller)
        case string: String =>
          if (string == "WALL") {
            Request(inputList, gamestate, controller)
          } else {
            Request(inputList, gamestate, controller)
          }
      }
  }

  val set3: Handler1 = {
    case Request(inputList, gamestate, controller) =>
      //controller.setDicedNumber(Some(0))
      controller.setPossibleFiguresTrueOrFalse(controller.gameboard.playerTurn.get.number)
      controller.setPossibleCellsTrueOrFalse(controller.gameboard.possibleCells.toList)
      controller.updatePlayerTurn(controller.nextPlayer(controller.gameboard.playerTurn.get.number - 1))
      gamestate.nextState(Roll(controller))
      controller.setStatementStatus(nextPlayer)
      Statements.value(StatementRequest(controller))
  }

  val set4: Handler1 = {
    case Request(inputList, gamestate, controller) =>
      controller.setStatementStatus(wrongWall)
      Statements.value(StatementRequest(controller))
  }

  val set: PartialFunction[Request, String] = (set1.andThen(set3).andThen(log)).orElse(set2.andThen(set4).andThen(log))

}
