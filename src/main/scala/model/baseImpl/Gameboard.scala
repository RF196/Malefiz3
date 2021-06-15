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
    string.append("Status: " + statementStatus.get.toString + "\n")
    Some(string.toString())
  }


  override def buildCompleteBoard(cellList: List[Cell]): Option[String] = {
    for {
      gameBoardString <- buildGameBoard()
      playerString <- buildPlayerString()
      infoString <- buildGameBoardInfo()
    } yield gameBoardString + playerString + infoString
  }


  override def getPossibleCells(startCell: Int, diceNumber: Int): Gameboard =

    var found: Set[Int] = Set[Int]()
    var needed: Set[Int] = Set[Int]()

      def recurse(currentCell: Int, diceNumber: Int): Unit =

        cells(currentCell).contains match
          case figure: Figure =>
          case string: String =>
            if string == "WALL" && diceNumber == 0 then
              needed += currentCell
            if string == "WALL" && diceNumber != 0 then
              return
      
        found += currentCell
        graph(currentCell)
          .foreach(cell => if (!found.contains(cell) && diceNumber != 0) recurse(cell, diceNumber - 1))
    
    recurse(startCell, diceNumber)
    copy(possibleCells = needed)

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

  override def createPlayer(name: String): Gameboard =
    copy(players = players :+ Some(Player(players.length + 1, name)))

  override def nextPlayer(playerNumber: Int): Player =
    if (playerNumber == players.length - 1)
      players.head.get
    else
      players(playerNumber + 1).get

  override def setPossibleCellsTrueOrFalse(cellNumbers: List[Int], state: String): Gameboard = {
    state match {
      case "3" => copy(cells = setPossibleCells(cells.length - 1, cellNumbers, cells)(markCell(boolean =
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
      case "2" => copy(cells = setPossibleFigures(cells.length - 1, cellNumber, cells)(markFigure(boolean = true)))
      case _ => copy(cells = setPossibleFigures(cells.length - 1, cellNumber, cells)(markFigure(boolean = false)))
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

  override def markFigure(boolean: Boolean)(cellNumber: Int): Cell = cells(cellNumber).copy(possibleFigures = boolean)

  override def setWall(cellNumber: Int): Gameboard = {
    cells(cellNumber).contains match {
      
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
    var a = 0;
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
    copy(cells = newCellList)

  override def placeFigure(cellNumber: Int, figureNumber: Int, playerNumber: Int): Gameboard =
    val a = cells(cellNumber).copy(contains = Figure(figureNumber, playerNumber))
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