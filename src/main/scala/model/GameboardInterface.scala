package model

import java.awt.Color

import controller.Statements
import controller.Statements.Statements
import model.baseImpl.{Cell, Figure, Gameboard, Player}

import scala.collection.mutable.Map
import scala.util.Random

trait GameboardInterface:

  def graph: Map[Int, Set[Int]]

  def cells: List[Cell]

  def players: List[Option[Player]]

  def playerTurn: Option[Player]

  def selectedFigure: Option[Figure]

  def dice: Option[Int]

  def possibleCells: Set[Int]

  def stateNumber: Option[Int]

  def statementStatus: Option[Statements]
  
  // Methods
  

  def buildPlayerString(): Option[String]
  
  def buildGameBoardString(start: Int): String

  def buildGameBoard(): Option[String]

  def buildGameBoardInfo(): Option[String]

  def buildCompleteBoard(cellList: List[Cell]): Option[String]
  
  
  def getPossibleCells(startCell: Int, diceNumber: Int): Gameboard

  def updateCells(newCells: List[Cell]): Gameboard

  //def updatePlayers(newPlayers: List[Player]): Gameboard

  def updatePlayerTurn(player: Player): Gameboard

  def updateSelectedFigure(newFigure: Figure): Gameboard

  def setDicedNumber(dicedNumber: Option[Int]): Gameboard

  def rollDice: Gameboard

  def updatePossibleCells(cells: Set[Int]): Gameboard

  def clearPossibleCells: Gameboard

  def updateStateNumber(newStateNumber: Int): Gameboard

  def updateStatementStatus(statements: Statements): Gameboard

  def createPlayer(name: String): Gameboard

  def nextPlayer(playerNumber: Int): Player

  def getPlayerFigure(playerNumber: Int, figureNumber: Int): Int

  def setPossibleCellsTrueOrFalse(listOfCellNumbers: List[Int], state: String): Gameboard

  def setPossibleFiguresTrueOrFalse(cellNumber: Int, stateNr: String): Gameboard
  def setPossibleFigures(cellListLength: Int, cellNumber: Int, cellList: List[Cell])(markFigures: Int => Cell): List[Cell]
  def markFigure(boolean: Boolean)(cellNumber: Int): Cell

  def setWall(cellNumber: Int): Gameboard
  
  def removeWall(n: Int): Gameboard

  def placeFigure(cellNumber: Int, figureNumber: Int, playerNumber: Int): Gameboard

  def removeFigure(playerNumber: Int, figureNumber: Int): Gameboard
  
  def getHomeNr(playerNumber: Int, figureNumber: Int): Int



