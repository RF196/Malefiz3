package controller.gamestates

import controller.{ControllerInterface, State}
import controller.gamestates._

case class Gamestate(controller: ControllerInterface):

  var currentState: State[Gamestate] = Setup(controller)

  def run(input: String) : Unit = currentState.handle(input, this)

  def nextState(state: State[Gamestate]) : Unit = this.currentState = state