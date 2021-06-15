package model.baseImpl

case class Cell(number: Int, 
                contains: Figure | String, 
                wallPermission: Boolean, 
                coord: Point, 
                possibleFigures: Boolean,
                possibleCells: Boolean):

  override def toString: String =
    contains match
      case f: Figure => "[" + f.playerNumber + "|" + f.number + "]"
      case s: String => 
        if (s == "WALL") {
          return "[W]"
        }
        else {
          return "[" + number + "]"
        }