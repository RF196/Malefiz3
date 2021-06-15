package model.baseImpl

import java.awt.Color

import controller.Statements
import controller.Statements.Statements
import model.GameboardInterface

import scala.collection.mutable.Map
import scala.util.Random

case class Gameboard(graph: Map[Int, Set[Int]],
                     cells: List[Cell],
                     players: List[Option[Player]],
                     playerTurn: Option[Player],
                     selectedFigure: Option[Figure],
                     dice: Option[Int],
                     possibleCells: Set[Int] = Set.empty,
                     stateNumber: Option[Int],
                     statementStatus: Option[Statements])extends GameboardInterface:

  override def getPossibleCells(startCell: Int, diceNumber: Int): Gameboard = ???

  override def updateCells(newCells: List[Cell]): Gameboard = copy(cells = newCells)

  //override def updatePlayers(newPlayers: List[Player]): Gameboard = copy(players = Option(newPlayers))

  override def updatePlayerTurn(player: Player): Gameboard = copy(playerTurn = Option(player))

  override def updateSelectedFigure(newFigure: Figure): Gameboard = copy(selectedFigure = Option(newFigure))

  override def setDicedNumber(dicedNumber: Option[Int]): Gameboard = copy(dice = dicedNumber)
  
  override def rollDice: Gameboard = copy(dice = Option(Random.nextInt(6) + 1))

  override def updatePossibleCells(cells: Set[Int]): Gameboard = copy(possibleCells = cells)

  override def clearPossibleCells: Gameboard = copy(possibleCells = Set.empty)

  override def updateStateNumber(newStateNumber: Int): Gameboard = copy(stateNumber = Option(newStateNumber))

  override def updateStatementStatus(statements: Statements): Gameboard = copy(statementStatus = Option(statements))

  override def createPlayer(name: String, color: Color): Gameboard =
    copy(players = players :+ Some(Player(players.length + 1, name, color)))
    
  override def nextPlayer(playerNumber: Int): Player =
    if (playerNumber == players.length - 1)
      players.head.get
    else
      players(playerNumber + 1).get

  override def setPossibleCellsTrueOrFalse(cellNumbers: List[Int], state: String): Gameboard = {
    state match {
      case "2" => copy(cells = setPossibleCells(cells.length - 1, cellNumbers, cells)(markCell(boolean =
        true)))
      case _ => copy(cells = setPossibleCells(cells.length - 1, cellNumbers, cells)(markCell(boolean = 
        false)))
    }
  }

  def setPossibleCells(cellListLength: Int, listOfCellNumbers: List[Int], listOfCells: List[Cell])(markCells: Int => Cell): List[Cell] =
    if (cellListLength == -1)
      listOfCells
    else if (listOfCellNumbers contains listOfCells(cellListLength).number)
      setPossibleCells(cellListLength - 1, listOfCellNumbers, listOfCells.updated(listOfCells(cellListLength).number,
        markCells(cellListLength)))(markCells)
    else
      setPossibleCells(cellListLength - 1, listOfCellNumbers, listOfCells)(markCells)

  def markCell(boolean: Boolean)(cellNumber: Int): Cell = cells(cellNumber).copy(possibleCells = boolean)
  
  
  override def setPossibleFiguresTrueOrFalse(cellNumber: Int, stateNr: String): Gameboard = {
    stateNr match {
      case "1" => copy(cells = setPossibleFigures(cells.length - 1, cellNumber, cells)(markFigure(boolean = true)))
      case _ => copy(cells =  setPossibleFigures(cells.length - 1, cellNumber, cells)(markFigure(boolean = false)))
    }
  }

  override def setPossibleFigures(cellListLength: Int, cellNumber: Int, cellList: List[Cell])(markFigures: Int => Cell): List[Cell] =
    if (cellListLength == -1)
      cellList
    else if (cellList(cellListLength).number == cellNumber)
      setPossibleFigures(
        cellListLength - 1,
        cellNumber,
        cellList.updated(cellList(cellListLength).number, markFigures(cellListLength))
      )(markFigures)
    else
      setPossibleFigures(cellListLength - 1, cellNumber, cellList)(markFigures)

  override def markFigure(boolean: Boolean)(cellNumber: Int): Cell = cells(cellNumber).copy(contains = "TEST")

  override def setWall(cellNumber: Int): Gameboard = {
    cells(cellNumber).contains match {
      case figure: Figure => return this
      case string: String =>
        if (string == "WALL") {
          return this
        } else {
          val newCell = cells(cellNumber).copy(contains = "WALL")
          val newCellList = cells.updated(cellNumber, newCell)
          copy(cells = newCellList)
        }
    }
  }

  override def getPlayerFigure(playerNumber: Int, figureNumber: Int): Int = {
    var a  = 0;
    cells.map(cell => {
      cell.contains match {
        case figure: Figure =>
          if (figure.number == figureNumber && figure.playerNumber == playerNumber) {
            a = cell.number
          }
        case string: String =>
      }
    })
    a
  }

  override def removeWall(cellNumber: Int): Gameboard =
    val newCell = cells(cellNumber).copy(contains = "EMPTY")
    val newCellList = cells.updated(cellNumber, newCell)
    copy(cells =  newCellList)
  
  override def placeFigure(cellNumber: Int, figureNumber: Int, playerNumber: Int, color: Color): Gameboard =
    val a = cells(cellNumber).copy(contains = Figure(figureNumber, playerNumber, color))
    copy(cells = cells.updated(cellNumber, a))
  
  override def removeFigure(playerNumber: Int, figureNumber: Int): Gameboard =
    val cellNumber = getPlayerFigure(playerNumber, figureNumber)
    val newCell = cells(cellNumber).copy(contains = "EMPTY")
    copy(cells = cells.updated(cellNumber, newCell))
  
  
  override def getHomeNr(playerNumber: Int, figureNumber: Int): Int =
    if (playerNumber == 1 && figureNumber == 1)
      0
    else
      (playerNumber - 1) * 5 + figureNumber - 1