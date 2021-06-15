package model.baseImpl

import scala.swing.Color

case class Figure(number: Int, playerNumber: Int, color: Color):

  override def toString: String = "" + number + "|" + playerNumber + ""
