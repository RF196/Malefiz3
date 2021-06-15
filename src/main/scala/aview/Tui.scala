package aview

import controller.ControllerInterface

import scala.swing.Reactor

class Tui(controller: ControllerInterface) extends Reactor:
  
  def checkInput(input: String): Unit = controller.checkInput(input).fold(l => println(l), r => processInput(input))
  
  def processInput(input: String): Unit =
    input match {
      case "load" =>
      case "save" =>
      case _ => controller.execute(input)
    }
