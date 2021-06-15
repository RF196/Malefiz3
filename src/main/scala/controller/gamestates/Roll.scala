package controller.gamestates

import controller.instructions.IRoll
import controller.{ControllerInterface, Request, State}


case class Roll(controller: ControllerInterface) extends State[Gamestate]:

  override def handle(input: String, gamestate: Gamestate): Unit = 
    IRoll.roll(Request(input.split(" ").toList, gamestate, controller))

  override def toString: String = "1"

