package controller

import controller.gamestates.Gamestate

case class Request(inputList: List[String], gamestate: Gamestate, controller: ControllerInterface)
case class StatementRequest(controller: ControllerInterface)
