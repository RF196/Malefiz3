package controller.instructions

import java.awt.Color

import controller.Statements._
import controller.gamestates.Roll
import controller.{InstructionInterface, Request, StatementRequest, Statements}

object ISetup extends InstructionInterface :

  val setup1: Handler0 = {
    case Request(inputList, gameState, controller)
      if inputList.contains("start") || controller.gameboard.players.length == 4 =>
      controller.updatePlayerTurn(controller.gameboard.players.head.get)
      Request(inputList, gameState, controller)
  }


  val setup2: Handler1 = {
    case Request(inputList, gameState, controller) =>
      gameState.nextState(Roll(controller))
      controller.setStatementStatus(roll)
      Statements.value(StatementRequest(controller))
  }


  val setup3: Handler1 = {
    case Request(inputList, gameState, controller) =>
      inputList(2) match {
        case "RED" => controller.createPlayer(inputList(1), Color.RED)
        case "GREEN" => controller.createPlayer(inputList(1), Color.GREEN)
        case "YELLOW" => controller.createPlayer(inputList(1), Color.YELLOW)
        case "BLUE" => controller.createPlayer(inputList(1), Color.BLUE)
      }

      controller.setStatementStatus(addPlayer)
      println(gameState.toString)
      Statements.value(StatementRequest(controller))
  }


  val setup: PartialFunction[Request, String] = setup1.andThen(setup2).orElse(setup3).andThen(log)

