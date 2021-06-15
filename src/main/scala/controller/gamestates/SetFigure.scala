package controller.gamestates

import controller.instructions.ISetFigure
import controller.{ControllerInterface, Request, State}

case class SetFigure(controller: ControllerInterface) extends State[Gamestate] {

  override def handle(input: String, gamestate: Gamestate): Unit = ISetFigure.set(Request(input.split(" ").toList, 
    gamestate, controller))
  override def toString: String = "3"

}
