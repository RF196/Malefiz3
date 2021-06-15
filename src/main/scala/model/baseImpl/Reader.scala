package model.baseImpl

import model.ReaderInterface
import scala.collection.mutable.Map
import scala.io.Source

case class Reader() extends ReaderInterface:

  val graphFile = "/Users/robert/IdeaProjects/malefiz3/src/main/resources/configuration/graph.txt"
  val cellsFile = "/Users/robert/IdeaProjects/malefiz3/src/main/resources/configuration/cells.txt"

  override def readFromGraphFile(): Map[Int, Set[Int]] =
    val graph: Map[Int, Set[Int]] = Map.empty
    val lines: Iterator[String] = Source.fromFile(graphFile).getLines()
    while (lines.hasNext) {
      val input = lines.next()
      val inputArray: Array[String] = input.split(" ")
      val keyValue = inputArray(0)
      inputArray.update(0, "")
      inputArray.map(input =>
        if (input != "") {
          updateGraph(keyValue.toInt, input.toInt, graph)
        })
    }
    graph
  
  def updateGraph(key: Int, value: Int, graph: Map[Int, Set[Int]]): Map[Int, Set[Int]] =
    graph.get(key)
      .map(_ => graph(key) += value)
      .getOrElse(graph(key) = Set[Int](value))
    graph
  
  override def readFromCellsFile(): List[Cell] =
     val cells :List[Cell] = Source.fromFile(cellsFile).getLines()
      .map(line => line.split(" "))
      .map {
        case Array(cn, pn, fn, wp, hw, x, y, pF, pC) =>
          if pn != "0" then
            Cell(cn.toInt, Figure(fn.toInt, pn.toInt), wp.toBoolean, Point(x.toInt, y.toInt), pF.toBoolean, pC
              .toBoolean)
          else if hw == "true" then
            Cell(cn.toInt, "WALL", wp.toBoolean, Point(x.toInt, y.toInt), pF.toBoolean, pC.toBoolean)
          else 
            Cell(cn.toInt, "Empty", wp.toBoolean, Point(x.toInt, y.toInt), pF.toBoolean, pC.toBoolean)
      }.toList
      cells