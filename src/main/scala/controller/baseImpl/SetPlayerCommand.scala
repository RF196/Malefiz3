package controller.baseImpl

import controller.State
import controller.Statements.Statements
import controller.gamestates.Gamestate
import model.GameboardInterface
import model.baseImpl.{Figure, Player, Gameboard}
import util.Command

class SetPlayerCommand(playerNumber: Int, playerFigure: Int, cellNumber: Int, controller: Controller) extends Command {

  var memento: Gameboard = controller.gameboard
  var mDicedNumber: Int = controller.gameboard.dice.get
  var mPlayersTurn: Player = controller.gameboard.playerTurn.get
  var mStatementStatus: Statements = controller.gameboard.statementStatus.get
  var mS: State[Gamestate] = controller.state.currentState

  override def doStep(): Unit = {
    
    controller.gameboard = controller.gameboard.removeFigure(playerNumber, playerFigure)
    val p = controller.gameboard.players(playerNumber - 1).get
    
    controller.gameboard.cells(cellNumber).contains match
      case f: Figure => controller.gameboard.placeFigure(cellNumber, playerFigure, p.number)
      case s: String => 
        if (s == "WALL") then
          controller.placeOrRemoveWall(cellNumber, boolean = false)
        else
         controller.gameboard.placeFigure(cellNumber, playerFigure, p.number)
  }

  override def undoStep(): Unit = {

    val new_memento = controller.gameboard
    val new_mDicedNumber = controller.gameboard.dice
    val new_mPlayersTurn = controller.gameboard.playerTurn.get
    val new_mS = controller.state
    val new_mStatementStatus = controller.gameboard.statementStatus.get

    controller.gameboard = memento
    controller.setDicedNumber(Some(mDicedNumber))
    controller.state.currentState = mS
    controller.updatePlayerTurn(mPlayersTurn)
    controller.setStatementStatus(mStatementStatus)

    memento = new_memento
    mDicedNumber = new_mDicedNumber.get
    mS = new_mS.currentState
    mPlayersTurn = new_mPlayersTurn
    mStatementStatus = new_mStatementStatus
  }

  override def redoStep(): Unit = doStep()
}
