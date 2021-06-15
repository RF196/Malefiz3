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

  def setPlayerTurn(player: Option[Player]): Gameboard

  def setSelectedFigure(playerNumber: Int, figureNumber: Int): Gameboard

  def setStateNumber(stateNumber: Int): Gameboard

  def setStatementStatus(statement: Statements): Gameboard

  def setPossibleCell(possibleCells: Set[Int]): Gameboard

  def rollDice(): Gameboard

  def setDicedNumber(dicedNumber: Option[Int]): Gameboard

  def clearPossibleCells: Gameboard

  def getPossibleCells(startCellNumber: Int, cube: Int): Gameboard


  def setPossibleCellsTrueOrFalse(listOfCellNumbers: List[Int], state: String): Gameboard

  def setPossibleCells(cellListLength: Int, listOfCellNumbers: List[Int], listOfCells: List[Cell])(markCells: Int => Cell): List[Cell]

  def markCell(boolean: Boolean)(cellNumber: Int): Cell


  def removeActualPlayerAndFigureFromCell(playerNumber: Int, figureNumber: Int): Gameboard

  def removePlayerFigureOnCell(cellNumber: Int): Cell

  def removePlayerOnCell(cellNumber: Int): Cell


  def setWall(cellNumber: Int): Gameboard

  def updateListWall(cellNumber: Int): List[Cell]

  def placeWall(cellNumber: Int): Cell


  def removeWall(cellNumber: Int): Gameboard

  def removeListWall(cellNumber: Int): List[Cell]

  def setHasWallFalse(cellNumber: Int): Cell


  def createPlayer(text: String): Gameboard

  def nextPlayer(playerList: List[Option[Player]], playerNumber: Int): Option[Player]

  def placeFigure(playerNumber: Int, figureNumber: Int, cellNumber: Int): Gameboard

  def getPlayerFigure(playerNumber: Int, figureNumber: Int): Int

  def getHomeNr(playerNumber: Int, figureNumber: Int): Int
  
  def setPossibleFiguresTrueOrFalse(cellNumber: Int, stateNr: String): Gameboard

  def setPossibleFigures(cellListLength: Int, cellNumber: Int, cellList: List[Cell])(function: Int => Cell): List[Cell]

  def markFigure(boolean: Boolean)(cellNumber: Int): Cell


trait DiceInterface:
  
  def rollDice: Option[Int]

