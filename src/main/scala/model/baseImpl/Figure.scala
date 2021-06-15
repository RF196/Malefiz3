package model.baseImpl

case class Figure(number: Int, playerNumber: Int):

  override def toString: String = "" + number + "|" + playerNumber + ""
