package controller.instructions

import controller.gamestates.{Roll, SelectFigure, SetWall, Setup}
import controller.{InstructionInterface, Request, StatementRequest, Statements}
import controller.Statements._
import model.baseImpl.Figure

object ISetFigure extends InstructionInterface {

  val set1: Handler0 = {
    case Request(inputList, gamestate, controller) =>
      
      val f = controller.gameboard.selectedFigure.get
      
      controller.gameboard.cells(inputList.head.toInt).contains match {
        case figure: Figure =>
        case string: String =>
          if (string == "EMPTY") {
            if ((inputList.head.toInt != 131) && controller.gameboard.possibleCells.contains(inputList.head.toInt))
              controller.placePlayerFigure(f.playerNumber, f.number, inputList.head.toInt)
          }
      }
      Request(inputList, gamestate, controller)
  }

  val select1: Handler1 = {
    case Request(inputList, gameState, controller)
      if inputList.length == 2 && inputList.head.toInt == controller.gameboard.selectedFigure.get._1 && inputList(
        1
      ).toInt == controller.gameboard.selectedFigure.get._2 =>
      controller.setPossibleCellsTrueOrFalse(controller.gameboard.possibleCells.toList)

      controller.resetPossibleCells
      gameState.nextState(SelectFigure(controller))
      controller.setStatementStatus(changeFigure)
      Statements.value(StatementRequest(controller))
  }

  val set2: Handler0 = {
              // wenn die Celle eine mauer hat und zu possibleCells gehört
    case Request(inputList, gamestate, controller) =>
      controller.gameboard.cells(inputList.head.toInt).contains match {
        case f: Figure =>
        case s: String =>
          if (s == "WALL") {
            controller.placePlayerFigure(controller.gameboard.selectedFigure.get.playerNumber,
              controller.gameboard.selectedFigure.get.number, inputList.head.toInt)
          }
      }
      Request(inputList, gamestate, controller)
  }

  val set3: Handler1 = {
    case Request(inputList, gamestate, controller) =>
      controller.setDicedNumber(Some(0))
      controller.setPossibleFiguresTrueOrFalse(controller.gameboard.playerTurn.get.number)
      controller.setPossibleCellsTrueOrFalse(controller.gameboard.possibleCells.toList)
      controller.resetPossibleCells
      controller.updatePlayerTurn(
        controller.nextPlayer(controller.gameboard.playerTurn.get.number - 1)
      )
      gamestate.nextState(Roll(controller))
      controller.setStatementStatus(nextPlayer)
      Statements.value(StatementRequest(controller))
  }

  val set4: Handler1 = {
    case Request(inputList, gamestate, controller) =>
      gamestate.nextState(SetWall(controller))
      controller.setStatementStatus(wall)
      controller.setPossibleFiguresTrueOrFalse(controller.gameboard.playerTurn.get.number)
      controller.setPossibleCellsTrueOrFalse(controller.gameboard.possibleCells.toList)
      controller.resetPossibleCells
      Statements.value(StatementRequest(controller))
  }
  val set5: Handler0 = {
    case Request(inputList, gamestate, controller) if inputList.head.toInt == 131 =>
      Request(inputList, gamestate, controller)
  }

  val set6: Handler1 = {
    case Request(inputList, gameState, controller) =>
      gameState.nextState(Setup(controller))
      controller.setStatementStatus(won)
      controller.weHaveAWinner()
      Statements.value(StatementRequest(controller))
  }

  val set7: Handler0 = {
    case Request(inputList, gamestate, controller)
      if !controller.gameboard.possibleCells.contains(inputList.head.toInt) || controller.gameboard
        .cells(inputList.head.toInt)
        .number == controller.gameboard.playerTurn.get.number =>
      Request(inputList, gamestate, controller)
  }

  val set8: Handler1 = {
    case Request(inputList, gamestate, controller) =>
      controller.setStatementStatus(wrongField)
      Statements.value(StatementRequest(controller))
  }

  val set: PartialFunction[Request, String] = 
    (set1
    .andThen(set3)
    .andThen(log))
    .orElse(select1.andThen(log))
    .orElse(set2.andThen(set4).andThen(log))
    .orElse(set5.andThen(set6).andThen(log))
    .orElse(set7.andThen(set8).andThen(log))

}
