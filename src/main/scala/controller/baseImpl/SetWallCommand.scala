package controller.baseImpl

import util.Command

class SetWallCommand(cellNumber: Int, controller: Controller) extends Command{

  var memento = controller.gameboard
  override def doStep(): Unit = {
    controller.gameboard = controller.gameboard.setWall(cellNumber)
  }

  override def undoStep(): Unit = {
    val new_memento = controller.gameboard
    controller.gameboard = memento
    memento = new_memento
  }

  override def redoStep(): Unit = doStep()

}
