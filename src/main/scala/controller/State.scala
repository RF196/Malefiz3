package controller

import controller.gamestates.Gamestate

trait State[T]:

  def handle(string: String, gamestate: T): Unit
