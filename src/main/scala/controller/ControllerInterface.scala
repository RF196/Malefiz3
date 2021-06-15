package controller

import controller.Statements.Statements
import controller.gamestates.Gamestate
import model.GameboardInterface
import model.baseImpl.{Gameboard, Player}

import scala.swing.event.Event
import scala.swing.{Color, Publisher}


trait ControllerInterface extends Publisher:

  def gameboard: GameboardInterface

  def gameBoardToString: Option[String]
  
  def undo(): Unit

  def redo(): Unit

  def weHaveAWinner(): Unit

  def rollCube: Option[Int]

  def setDicedNumber(dicedNumber: Option[Int]): Unit

  def createPlayer(name: String): Unit

  def nextPlayer(playerList: List[Option[Player]], playerNumber: Int): Option[Player]

  def setPlayersTurn(player: Option[Player]): Unit

  def placePlayerFigure(playerNumber: Int, playerFigure: Int, cellNumber: Int): Unit

  def setSelectedFigure(playerNumber: Int, figureNumber: Int): Unit

  def getFigurePosition(playerNumber: Int, figureNumber: Int): Int

  def resetPossibleCells(): Unit

  def setStateNumber(n: Int): Unit

  def calculatePath(startCell: Int, diceNumber: Int): Unit

  def removeActualPlayerAndFigureFromCell(playerNumber: Int, figureNumber: Int): Unit

  def placeFigure(playerNumber: Int, figureNumber: Int, cellNumber: Int): Unit
  
  def placeOrRemoveWall(n: Int, boolean: Boolean): Unit

  def getGameState: Gamestate

  def setStatementStatus(statement: Statements): Unit

  def setPossibleCells(possibleCells: Set[Int]): GameboardInterface

  def setPossibleFiguresTrueOrFalse(playerNumber: Int): Unit

  def setPossibleCellsTrueOrFalse(toList: List[Int]): Unit

  def execute(string: String): Unit

  def checkInput(input: String): Either[String, String]

  def save(): Unit

  def load(): Unit


class GameBoardChanged extends Event

class Winner extends Event


