package controller.instructions

import controller.Statements._
import controller.gamestates.Roll
import controller.{InstructionInterface, Request, StatementRequest, Statements}

object ISetup extends InstructionInterface:

  val setup1: Handler0 =
    case Request(inputList, gameState, controller)
      if inputList.contains("start") ||
        controller.gameboard.players.length == 4 =>
      controller.setPlayersTurn(controller.gameboard.players.head)
      Request(inputList, gameState, controller)


  val setup2: Handler1 =
    case Request(inputList, gameState, controller) =>
      gameState.nextState(Roll(controller))
      controller.setStatementStatus(roll)
      Statements.value(StatementRequest(controller))


  val setup3: Handler1 =
    case Request(inputList, gameState, controller) =>
      controller.createPlayer(inputList(1))
      controller.setStatementStatus(addPlayer)
      Statements.value(StatementRequest(controller))
  
  val setup: PartialFunction[Request, String] = setup1.andThen(setup2).orElse(setup3).andThen(log)

