package controller.gamestates

import controller.{ControllerInterface, Request, State}
import controller.baseImpl.Controller
import controller.instructions.ISetup

case class Setup(controller: ControllerInterface) extends State[Gamestate]:

  override def handle(input: String, gamestate: Gamestate): Unit = 
    ISetup.setup(Request(input.split(" ").toList, gamestate, controller))

  override def toString: String = "1"
