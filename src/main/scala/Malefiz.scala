import aview.{GameGui, Gui, PlayerGui, Tui}
import controller.baseImpl.Controller
import model.baseImpl.{Gameboard, Reader}
import model.{GameboardInterface, ReaderInterface}

import scala.io.StdIn.readLine

@main def malefiz: Unit =
  println("Welcome to Malefiz")

  val reader: ReaderInterface = Reader()
  val gameboard: Gameboard = Gameboard(
    reader.readFromGraphFile(),
    reader.readFromCellsFile(),
    players = List.empty,
    playerTurn = None,
    selectedFigure = None,
    dice = None,
    stateNumber = None,
    statementStatus = None)

  val controller: Controller = new Controller(gameboard)
  val tui: Tui = new Tui(controller)
  val entryGui = new Gui(controller)

  var input: String = ""

  while (input != "end") {
    input = readLine()
    tui.checkInput(input)
  }
  println(msg)

def msg = "I was compiled by Scala 3. :)\n"

