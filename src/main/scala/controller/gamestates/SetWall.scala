package controller.gamestates

import controller.{ControllerInterface, Request, State}
import controller.instructions._

case class SetWall(controller: ControllerInterface) extends State[Gamestate] {

  override def handle(input: String, gamestate: Gamestate): Unit = ISetWall.set(Request(input.split(" ").toList, 
    gamestate, controller))
  override def toString: String = "5"

}
