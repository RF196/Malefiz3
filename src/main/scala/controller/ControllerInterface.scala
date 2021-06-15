package controller

import controller.Statements.Statements
import controller.gamestates.Gamestate
import model.GameboardInterface
import model.baseImpl.{Gameboard, Player}

import scala.swing.event.Event
import scala.swing.{Color, Publisher}

class GameBoardChanged extends Event
class Winner extends Event

trait ControllerInterface extends Publisher:
  
  def gameboard: GameboardInterface
  
  def getGameState: Gamestate

  def execute(input: String): Unit

  def checkInput(input: String): Either[String, String]

  def calculatePath(startCell: Int, diceNumber: Int): Unit

  def setStatementStatus(statement: Statements): Unit

  def createPlayer(name: String, color: Color): Unit

  def updatePlayerTurn(player: Player): Unit

  def nextPlayer(playerNumber: Int) : Player
  
  def rollDice: Unit
  
  def setDicedNumber(n: Option[Int]): Unit

  def getPlayerFigureLocation(playerNumber: Int, figureNumber: Int): Int
  
  def setSelectedFigure(player: Player, figureNumber: Int): Unit

  def setPossibleCellsTrueOrFalse(availableCells: List[Int]): Unit

  def setPossibleFiguresTrueOrFalse(playerNumber: Int): Unit
  
  def resetPossibleCells: Unit

  def placePlayerFigure(playerNumber: Int, playerFigure: Int, cellNumber: Int): Unit

  def placeOrRemoveWall(n: Int, boolean: Boolean): Unit
  
  def resetGameBoard(): Unit
  
  def save(): Unit

  def load(): Unit
  
  def undo(): Unit
  
  def redo(): Unit

  def weHaveAWinner(): Unit

  




