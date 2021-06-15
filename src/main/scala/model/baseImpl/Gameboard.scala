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
                     statementStatus: Option[Statements])extends GameboardInterface :


  override def buildPlayerString(): Option[String] = {
    Some(
      s"""|${cells.slice(0, 20).mkString("")}
          |""".stripMargin)
  }

  val buildRow = (start: Int, stop: Int) => start match {
    case 130 => s"""|${cells.slice(start, stop).mkString("")}
                    |""".stripMargin
    case _ => buildGameBoardString(stop) +
      s"""|${cells.slice(start, stop).mkString("")}
          |""".stripMargin
  }

  override def buildGameBoardString(start: Int): String = {
    start match {
      case 20 | 42 | 94 | 113 => buildRow(start, start + 17)
      case 37 => buildRow(start, start + 5)
      case 59 => buildRow(start, start + 4)
      case 63 => buildRow(start, start + 13)
      case 76 | 87 | 111 => buildRow(start, start + 2)
      case 78 => buildRow(start, start + 9)
      case 89 => buildRow(start, start + 5)
      case 93 => buildRow(start, start + 1)
      case 130 => buildRow(start, start + 1)
    }
  }

  override def buildGameBoard(): Option[String] = Some(buildGameBoardString(20))

  override def buildGameBoardInfo(): Option[String] = {
    val string = new StringBuilder("Malefiz-GameBoard\n\n")
    string.append("Players: ")
    players.flatMap(player => player.map(x => string.append(x.toString + " / ")))
    string.append("\n" + "Players turn: " + playerTurn.getOrElse("No registered players").toString + "\n")
    string.append("Dice: " + dice.getOrElse("Dice is not rolled yet").toString + "\n")
    string.append("Possible Cells: " + possibleCells.toString + "\n")
    string.append("Status: " + statementStatus.getOrElse("No State").toString + "\n")
    Some(string.toString())
  }


  override def buildCompleteBoard(cellList: List[Cell]): Option[String] = {
    for {
      gameBoardString <- buildGameBoard()
      playerString <- buildPlayerString()
      infoString <- buildGameBoardInfo()
    } yield gameBoardString + playerString + infoString
  }


  override def setPlayerTurn(player: Option[Player]): Gameboard = copy(playerTurn = player)

  override def setSelectedFigure(playerNumber: Int, figureNumber: Int): Gameboard =
    copy(selectedFigure = Some(Figure(figureNumber, playerNumber)))

  override def setStateNumber(stateNumber: Int): Gameboard = copy(stateNumber = Option(stateNumber))

  override def setStatementStatus(statement: Statements): Gameboard = copy(statementStatus = Option(statement))

  override def setPossibleCell(possibleCells: Set[Int]): Gameboard = copy(possibleCells = possibleCells)

  override def rollDice(): Gameboard = copy(dice = Dice().rollDice)

  override def setDicedNumber(dicedNumber: Option[Int]): Gameboard = copy(dice = dicedNumber)

  override def clearPossibleCells: Gameboard = copy(possibleCells = Set.empty)

  override def getPossibleCells(startCell: Int, diceNumber: Int): Gameboard = {

    var found: Set[Int] = Set[Int]()
    var needed: Set[Int] = Set[Int]()

    def recurse(currentCell: Int, diceNumber: Int): Unit = {

      cells(currentCell).contains match
        case figure: Figure =>
          if (figure.playerNumber != playerTurn.get.number && diceNumber == 0) {
            needed += currentCell
          }
        case string: String =>
          if diceNumber == 0 || string == "WALL" && diceNumber == 0 then
            needed += currentCell
          if string == "WALL" && diceNumber != 0 then
            return

      found += currentCell
      graph(currentCell).foreach(x => if (!found.contains(x) && diceNumber != 0) recurse(x, diceNumber - 1))
    }
    recurse(startCell, diceNumber)
    copy(possibleCells = needed)
  }

  override def setPossibleCellsTrueOrFalse(cellNumbers: List[Int], state: String): Gameboard = {
    state match {
      case "2" => copy(cells = setPossibleCells(cells.length - 1, cellNumbers, cells)(markCell(boolean = true)))
      case _ => copy(cells = setPossibleCells(cells.length - 1, cellNumbers, cells)(markCell(boolean = false)))
    }
  }

  override def setPossibleCells(cellListLength: Int, listOfCellNumbers: List[Int], listOfCells: List[Cell])(markCells: Int => Cell): List[Cell] =
    if (cellListLength == -1)
      listOfCells
    else if (listOfCellNumbers contains listOfCells(cellListLength).number)
      setPossibleCells(cellListLength - 1, listOfCellNumbers, listOfCells.updated(listOfCells(cellListLength).number, 
        markCells(cellListLength)))(markCells)
    else
      setPossibleCells(cellListLength - 1, listOfCellNumbers, listOfCells)(markCells)

  override def markCell(boolean: Boolean)(cellNumber: Int): Cell = cells(cellNumber).copy(possibleCells = boolean)


  override def removeActualPlayerAndFigureFromCell(playerNumber: Int, figureNumber: Int): Gameboard = {
    val cell = getPlayerFigure(playerNumber, figureNumber)
    copy(cells = cells.updated(cell, removePlayerFigureOnCell(cell)))
    copy(cells = cells.updated(cell, removePlayerOnCell(cell)))
  }

  override def removePlayerFigureOnCell(cellNumber: Int): Cell = cells(cellNumber).copy(contains = "EMPTY")

  override def removePlayerOnCell(cellNumber: Int): Cell = cells(cellNumber).copy(contains = "EMPTY")

  override def setWall(cellNumber: Int): Gameboard = copy(cells = updateListWall(cellNumber))

  override def updateListWall(cellNumber: Int): List[Cell] =
    if (cellNumber >= 42) {
      cells(cellNumber).contains match {
        case figure: Figure =>
          cells.updated(cellNumber, placeWall(cellNumber))
        case string: String =>
          if (string == "WALL") {
            cells
          } else {
            cells.updated(cellNumber, placeWall(cellNumber))
          }
      }
    } else {
      cells
    }

  override def placeWall(cellNumber: Int): Cell = cells(cellNumber).copy(contains = "WALL")

  override def removeWall(cellNumber: Int): Gameboard = copy(cells = removeListWall(cellNumber))

  override def removeListWall(cellNumber: Int): List[Cell] = cells.updated(cellNumber, setHasWallFalse(cellNumber))

  override def setHasWallFalse(cellNumber: Int): Cell = cells(cellNumber).copy(contains = "EMPTY")

  override def createPlayer(text: String): Gameboard = copy(players = players :+ Some(Player(players.length + 1, text)))

  override def nextPlayer(playerList: List[Option[Player]], playerNumber: Int): Option[Player] =
    if (playerNumber == playerList.length - 1)
      playerList.head
    else
      playerList(playerNumber + 1)
      
  
  override def placeFigure(playerNumber: Int, figureNumber: Int, cellNumber: Int): Gameboard = {
    val g = cells(cellNumber).copy(contains = Figure(figureNumber, playerNumber))
    copy(cells = cells.updated(cellNumber, g))
  }
  
  override def getPlayerFigure(playerNumber: Int, figureNumber: Int): Int =
    var cellNumber = 0
    cells.foreach(cell => {
      cell.contains match {
        case figure: Figure =>
          if (figure.playerNumber == playerNumber && figure.number == figureNumber)
            cellNumber = cell.number
        case string: String =>
      }
    })
    cellNumber

  override def getHomeNr(playerNumber: Int, figureNumber: Int): Int =
    if (playerNumber == 1 && figureNumber == 1)
      0
    else
      (playerNumber - 1) * 5 + figureNumber - 1

  

  override def setPossibleFiguresTrueOrFalse(cellNumber: Int, stateNr: String): Gameboard = {
    stateNr match {
      case "1" => copy(cells = setPossibleFigures(cells.length - 1, cellNumber, cells)(markFigure(boolean = true)))
      case _ => copy(cells = setPossibleFigures(cells.length - 1, cellNumber, cells)(markFigure(boolean = false)))
    }
  }

  // TODO
  override def setPossibleFigures(cellListLength: Int, cellNumber: Int, cellList: List[Cell])(markFigures: Int => Cell): List[Cell] =
    if (cellListLength == -1) {
      cellList
    } else {
      cellList(cellListLength).contains match {
        case figure: Figure =>
          if (figure.playerNumber == cellNumber) {
            setPossibleFigures(cellListLength - 1, cellNumber, cellList.updated(cellList(cellListLength).number, markFigures(cellListLength)))(markFigures)
          } else {
            setPossibleFigures(cellListLength - 1, cellNumber, cellList)(markFigures)
          }
        case string: String =>
          setPossibleFigures(cellListLength - 1, cellNumber, cellList)(markFigures)
      }
    }

  override def markFigure(boolean: Boolean)(cellNumber: Int): Cell = cells(cellNumber).copy(possibleFigures = boolean)
