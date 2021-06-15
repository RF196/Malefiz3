package model.baseImpl

case class Cell(number: Int, 
                contains: Figure | String, 
                wallPermission: Boolean, 
                coord: Point, 
                possibleFigures: Boolean,
                possibleCells: Boolean):
  
  val stoneOrPlayer: String =
    if (number < 20) {
      contains match {
        case figure: Figure =>
          if (possibleCells) {
            "" + figure.playerNumber + "|" + figure.number +" "
          } else {
            figure.playerNumber + ""
          }
        case string: String =>
          "_"
      }
    } else {
      contains match {
        case figure: Figure =>
          if (possibleCells) {
            "" + figure.playerNumber + "|" + number + ""
          } else {
            number + ""
          }
        case string: String =>
          if (string == "WALL") {
            if (possibleCells) {
              "X" + "|" + number
            } else {
              "X"
            }
          }
          if (string == "EMPTY") {
            if (possibleCells) {
              number + ""
            } else {
              "_"
            }
          } else {
            "_"
          }
      }
    }
  
  override def toString: String = stoneOrPlayer
    