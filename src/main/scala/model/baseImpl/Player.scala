package model.baseImpl

case class Player(number: Int, name: String):
  
  override def toString: String = name
