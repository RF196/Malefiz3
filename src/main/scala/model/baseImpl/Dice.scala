package model.baseImpl

import model.DiceInterface

case class Dice() extends DiceInterface:

  def rollDice: Option[Int] =
    val randomNumber = Option(scala.util.Random.nextInt(6) + 1)
    randomNumber
  
