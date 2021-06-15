package controller.baseImpl

import aview.{GameGui, StartGui, PlayerGui}
import controller.Statements.Statements
import controller.gamestates.Gamestate
import controller.{ControllerInterface, GameBoardChanged, Statements, Winner}

import model.GameboardInterface
import model.baseImpl.{Figure, Gameboard, Player}
import util.UndoManager

import scala.swing.{Color, Publisher}

class Controller(var gameboard: Gameboard) extends ControllerInterface with Publisher:

  val state: Gamestate = Gamestate(this)
  val mementoGameBoard: GameboardInterface = gameboard
  val undoManager = new UndoManager

  override def gameBoardToString: Option[String] = gameboard.buildCompleteBoard(gameboard.cells)
  
  override def undo(): Unit = {
    undoManager.undoStep()
    publish(new GameBoardChanged)
  }

  override def redo(): Unit = {
    undoManager.redoStep()
    publish(new GameBoardChanged)
  }

  override def weHaveAWinner(): Unit = publish(new Winner)

  override def rollCube: Option[Int] = {
    gameboard = gameboard.rollDice()
    gameboard.dice
  }

  override def setDicedNumber(dicedNumber: Option[Int]): Unit = gameboard = gameboard.setDicedNumber(dicedNumber)

  override def createPlayer(name: String): Unit = {
    gameboard = gameboard.createPlayer(name)
    publish(new GameBoardChanged)
  }

  override def nextPlayer(playerList: List[Option[Player]], playerNumber: Int): Option[Player] =
    gameboard.nextPlayer(playerList, playerNumber)

  override def setPlayersTurn(player: Option[Player]): Unit = {
    gameboard = gameboard.setPlayerTurn(player)
    publish(new GameBoardChanged)
  }

  override def placePlayerFigure(playerNumber: Int, playerFigure: Int, cellNumber: Int): Unit = {
    undoManager.doStep(new SetPlayerCommand(playerNumber, playerFigure, cellNumber, this))
    publish(new GameBoardChanged)
  }

  override def setSelectedFigure(playerNumber: Int, figureNumber: Int): Unit = {
    gameboard = gameboard.setSelectedFigure(playerNumber, figureNumber)
    publish(new GameBoardChanged)
  }

  override def getFigurePosition(playerNumber: Int, figureNumber: Int): Int = {
    val position = gameboard.getPlayerFigure(playerNumber, figureNumber)
    position
  }

  override def resetPossibleCells(): Unit = gameboard = gameboard.clearPossibleCells

  override def setStateNumber(stateNumber: Int): Unit = {
    gameboard = gameboard.setStateNumber(stateNumber)
    publish(new GameBoardChanged)
  }

  override def calculatePath(startCell: Int, diceNumber: Int): Unit = {
    gameboard = gameboard.getPossibleCells(startCell, diceNumber)
    publish(new GameBoardChanged)
  }

  override def removeActualPlayerAndFigureFromCell(playerNumber: Int, figureNumber: Int): Unit = {
    gameboard = gameboard.removeActualPlayerAndFigureFromCell(playerNumber, figureNumber)
    publish(new GameBoardChanged)
  }
  
  override def placeFigure(playerNumber: Int, figureNumber: Int, cellNumber: Int): Unit = {
    gameboard = gameboard.placeFigure(playerNumber, figureNumber, cellNumber)
    publish(new GameBoardChanged)
  }

  override def placeOrRemoveWall(n: Int, boolean: Boolean): Unit = {
    boolean match {
      case true => undoManager.doStep(new SetWallCommand(n, this))
      case false => gameboard = gameboard.removeWall(n)
    }
    publish(new GameBoardChanged)
  }

  override def getGameState: Gamestate = this.state

  override def setStatementStatus(statement: Statements): Unit = {
    gameboard = gameboard.setStatementStatus(statement)
    publish(new GameBoardChanged)
  }

  override def setPossibleCells(possibleCells: Set[Int]): GameboardInterface = gameboard.setPossibleCell(possibleCells)

  override def setPossibleCellsTrueOrFalse(availableCells: List[Int]): Unit = {
    gameboard = gameboard.setPossibleCellsTrueOrFalse(availableCells, state.currentState.toString)
    publish(new GameBoardChanged)
  }

  override def setPossibleFiguresTrueOrFalse(playerNumber: Int): Unit = {
    gameboard = gameboard.setPossibleFiguresTrueOrFalse(playerNumber, state.currentState.toString)
    publish(new GameBoardChanged)
  }

  override def execute(input: String): Unit = state.run(input)

  override def checkInput(input: String): Either[String, String] = {
    if (state.currentState.toString == "4")
      if(input.split(" ").toList.size != 2)
        return Left("Bitte Spieler in Form von : 'n Spielername' eintippen \n " +
          "und mit 'n start' dann starten!")
      else
        return Right(input)
    else
     Right(input)
}

  override def save(): Unit = ???

  override def load(): Unit = ???
  