package model

import model.baseImpl.Cell
import scala.collection.mutable.Map

trait ReaderInterface:

  def readFromGraphFile(): Map[Int, Set[Int]]
  def readFromCellsFile(): List[Cell]
