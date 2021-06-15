package aview

import controller.{ControllerInterface, GameBoardChanged}

import scala.swing.Reactor

class Tui(controller: ControllerInterface) extends Reactor:

  listenTo(controller)
  
  def checkInput(input: String): Unit = controller.checkInput(input).fold(l => println(l), r => processInput(input))
  
  def processInput(input: String): Unit =
    input match {
      case "load" =>
      case "save" =>
      case "exit" => System.exit(0)
      case _ => controller.execute(input)
    }

  reactions += {
    case _: GameBoardChanged => update()
  }

  def update(): Unit = {
    textPrint(controller.gameBoardToString.get)
  }

  def textPrint(str: String): Unit = println(str)
