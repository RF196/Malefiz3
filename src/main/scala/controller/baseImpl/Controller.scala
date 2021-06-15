package controller.baseImpl

import aview.{GameGui, Gui, PlayerGui}
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
  
  override def getGameState: Gamestate = this.state
  
  override def execute(input: String): Unit = state.run(input)

  override def checkInput(input: String): Either[String, String] =
    if (state.currentState.toString == "1")
      if(input.split(" ").toList.size != 3)
        return Left("Bitte Spieler in Form von : 'n Spielername Farbe' eintippen und mit 'n start game' dann starten!")
      else
        return Right(input)
    else
      Right(input)

  override def calculatePath(startCell: Int, diceNumber: Int): Unit = {
    gameboard = gameboard.getPossibleCells(startCell, diceNumber)
    publish(new GameBoardChanged)
  }
  
  override def setStatementStatus(statement: Statements): Unit =
    gameboard = gameboard.updateStatementStatus(statement)
    publish(new GameBoardChanged)
  
  override def createPlayer(name: String, color: Color): Unit =
    println("Lege Spieler: " + name + "an")
    gameboard = gameboard.createPlayer(name, color)
    publish(new GameBoardChanged)
  
  override def updatePlayerTurn(player: Player): Unit =
    gameboard = gameboard.updatePlayerTurn(player)
    publish(new GameBoardChanged)

  override def nextPlayer(playerNumber: Int) : Player =
    gameboard.nextPlayer(playerNumber)
  
  override def rollDice: Unit = 
    gameboard = gameboard.rollDice

  override def setDicedNumber(n: Option[Int]): Unit = {
    gameboard = gameboard.setDicedNumber(n)
  }


  override def getPlayerFigureLocation(playerNumber: Int, figureNumeber: Int): Int = {
    val position = gameboard.getPlayerFigure(playerNumber, figureNumeber)
    position
  }

  override def resetPossibleCells: Unit = gameboard = gameboard.clearPossibleCells

  override def setPossibleCellsTrueOrFalse(availableCells: List[Int]): Unit = {
    gameboard = gameboard.setPossibleCellsTrueOrFalse(availableCells, state.currentState.toString)
    publish(new GameBoardChanged)
  }
  
  override def setPossibleFiguresTrueOrFalse(playerNumber: Int): Unit = {
    gameboard = gameboard.setPossibleFiguresTrueOrFalse(playerNumber, state.currentState.toString)
    publish(new GameBoardChanged)
  }

  override def placePlayerFigure(playerNumber: Int, playerFigure: Int, cellNumber: Int): Unit = {
    undoManager.doStep(new SetPlayerCommand(playerNumber, playerFigure, cellNumber, this))
    publish(new GameBoardChanged)
  }

  override def setSelectedFigure(player: Player, figureNumber: Int): Unit =
    gameboard = gameboard.updateSelectedFigure(Figure(figureNumber, player.number, player.color))
    publish(new GameBoardChanged)

  override def placeOrRemoveWall(n: Int, boolean: Boolean): Unit = {
    boolean match {
      case true => undoManager.doStep(new SetWallCommand(n, this))
      case false => gameboard = gameboard.removeWall(n)
    }
    publish(new GameBoardChanged)
  }
  
  override def save(): Unit = ???

  override def load(): Unit = ???

  override def redo(): Unit = ???

  override def undo(): Unit = ???

  override def resetGameBoard(): Unit = ???

  override def weHaveAWinner(): Unit = publish(new Winner)