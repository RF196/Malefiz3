package controller.gamestates

import controller.instructions.ISelectFigure
import controller.{ControllerInterface, Request, State}

case class SelectFigure(controller: ControllerInterface) extends State[Gamestate] {

  override def handle(input: String, gamestate: Gamestate): Unit =
    ISelectFigure.select(Request(input.split(" ").toList, gamestate, controller))
  override def toString: String = "3"

}
