package aview

import java.awt.image.BufferedImage
import java.awt.{BasicStroke, Color, Font}
import java.io.File

import controller.gamestates.SelectFigure
import controller.{ControllerInterface, GameBoardChanged, Statements}
import javax.imageio.ImageIO
import javax.swing.ImageIcon
import javax.swing.text.StyleConstants
import model.baseImpl.Figure
import controller.StatementRequest
import controller.Statements._
import controller.Winner

import scala.swing.event.{ButtonClicked, Key, MouseClicked}
import scala.swing.{Action, Button, Dialog, Dimension, Frame, Graphics2D, GridBagPanel, Label, Menu, MenuBar, MenuItem, Panel, Swing, TextArea, TextPane}

class GameGui(controller: ControllerInterface) extends Frame {
  
  val image: BufferedImage = ImageIO.read(new File("src/main/scala/de/htwg/se/malefiz/aview/gui/malefizimg.png"))
  val g2d: Graphics2D = image.createGraphics()

  title = "Malefiz"
  centerOnScreen()
  visible = false

  var mouseX: Set[Int] = Set().empty
  var mouseY: Set[Int] = Set().empty

  val thick = new BasicStroke(3f)

  val playerLabel = new Label("Player")
  playerLabel.foreground = Color.WHITE
  playerLabel.background = Color.DARK_GRAY
  playerLabel.font = new Font("Sans Serif", Font.ITALIC, 20)
  playerLabel.border = Swing.EmptyBorder(3)

  var playerArea = new TextPane
  playerArea.background = Color.DARK_GRAY
  playerArea.font = new Font("Sans Serif", Font.ITALIC, 16)
  playerArea.border = Swing.EmptyBorder(3)
  playerArea.editable = false

  val playerTurnLabel = new Label("Turn")
  playerTurnLabel.foreground = Color.WHITE
  playerTurnLabel.background = Color.DARK_GRAY
  playerTurnLabel.border = Swing.EmptyBorder(3)
  playerTurnLabel.font = new Font("Sans Serif", Font.ITALIC, 20)

  val playerTurnArea = new Label("")
  playerTurnArea.foreground = Color.WHITE
  playerTurnArea.background = Color.DARK_GRAY
  playerTurnArea.border = Swing.EmptyBorder(3)
  playerTurnArea.font = new Font("Sans Serif", Font.ITALIC, 16)

  val cubeLabel = new Label("Dice")
  cubeLabel.foreground = Color.WHITE
  cubeLabel.background = Color.DARK_GRAY
  cubeLabel.font = new Font("Sans Serif", Font.ITALIC, 18)
  cubeLabel.border = Swing.EmptyBorder(3)

  val cubeButton = new Button()
  val cubeIcon = new ImageIcon(
    "src/main/scala/de/htwg/se/malefiz/aview/gui/dice.png"
  )
  cubeButton.icon = cubeIcon
  cubeButton.border = Swing.EmptyBorder(3)

  val randomNumberLabel = new Label("Cube shows: ")
  randomNumberLabel.foreground = Color.WHITE
  randomNumberLabel.background = Color.DARK_GRAY
  randomNumberLabel.font = new Font("Sans Serif", Font.ITALIC, 20)
  randomNumberLabel.border = Swing.EmptyBorder(3)

  val randomNumberArea = new Label("")
  randomNumberArea.foreground = Color.WHITE
  randomNumberArea.background = Color.DARK_GRAY
  randomNumberArea.font = new Font("Sans Serif", Font.ITALIC, 16)
  randomNumberArea.border = Swing.EmptyBorder(3)

  val informationArea = new TextArea("")
  informationArea.foreground = Color.WHITE
  informationArea.background = Color.DARK_GRAY
  informationArea.font = new Font("Sans Serif", Font.ITALIC, 20)
  informationArea.border = Swing.EmptyBorder(3)
  informationArea.editable = false

  val panel: Panel = new Panel {

    override def paint(g: Graphics2D): Unit =
      g.drawImage(image, 0, 0, null)

    preferredSize = new Dimension(image.getWidth(null), image.getHeight())
    listenTo(mouse.clicks)

    reactions += {

      case MouseClicked(_, p, _, 1, _) =>
        mouseX = getRange(p.x)
        mouseY = getRange(p.y)
        val state = controller.getGameState.currentState
        if (state.isInstanceOf[SelectFigure])
          controller.gameboard.cells.map(cell =>
            if (mouseX.contains(cell.coord.x) && mouseY.contains(cell.coord.y)) {
              cell.contains match {
                case figure: Figure =>
                  controller.execute(figure.playerNumber + " " + figure.number)
                  drawGameBoard()
                  updateInformationArea()
                case string: String =>
              }
            }
          )
        else
          controller.gameboard.cells.map(cell =>
            if (mouseX.contains(cell.coord.x) && mouseY.contains(cell.coord.y)) {
              cell.contains match {
                case figure: Figure =>
                  if (figure.playerNumber == controller.gameboard.selectedFigure.get.playerNumber && figure.number ==
                    controller.gameboard.selectedFigure.get.number) {
                    controller.execute(figure.playerNumber + " " + figure.number)
                    controller.setStatementStatus(changeFigure)
                    updateInformationArea()
                    drawGameBoard()
                  }
                case string: String =>
                  controller.execute(cell.number.toString)
                  drawGameBoard()
                  updatePlayerTurn()
                  updateInformationArea()
              }
            }
          )
    }
  }

  def updatePlayerArea(): Boolean = {
    val doc = playerArea.styledDocument

    controller.gameboard.players.indices.map { i =>
      val playerString = " Spieler" + (i + 1) + ": " + controller.gameboard.players(i).getOrElse("") + "\n"
      i match {
        case 0 =>
          val red = playerArea.styledDocument.addStyle("Red", null)
          StyleConstants.setForeground(red, Color.RED)
          doc.insertString(doc.getLength, playerString, red)
          true
        case 1 =>
          val green = playerArea.styledDocument.addStyle("Green", null)
          StyleConstants.setForeground(green, Color.GREEN)
          doc.insertString(doc.getLength, playerString, green)
          true
        case 2 =>
          val yellow = playerArea.styledDocument.addStyle("Yellow/Orange", null)
          StyleConstants.setForeground(yellow, Color.ORANGE)
          doc.insertString(doc.getLength, playerString, yellow)
          true
        case 3 =>
          val blue = playerArea.styledDocument.addStyle("Blue", null)
          StyleConstants.setForeground(blue, Color.BLUE)
          doc.insertString(doc.getLength, playerString, blue)
          true
      }
    }
    true
  }

  def getRange(pixel: Int): Set[Int] = {
    var lowR = pixel
    var highR = pixel
    var range: Set[Int] = Set().empty

    LazyList.range(0, 20).foreach(i => {
      lowR = lowR - 1
      range += lowR
      highR = highR + 1
      range += highR
    })

    range
  }

  def updatePlayerTurn(): Boolean = {
    playerTurnArea.text = controller.gameboard.playerTurn.get.name
    true
  }

  def updateRandomNumberArea(): Boolean = {
    randomNumberArea.text = controller.gameboard.dice.toString
    true
  }

  def updateInformationArea(): Boolean = {
    this.informationArea.text =
      Statements.value(StatementRequest(controller))
    true
  }

  def drawGameBoard(): Unit = {
    controller.gameboard.cells.map(cell =>
      cell.contains match {
        case figure: Figure =>
          if (figure.playerNumber == 1) {
            this.drawCircle(cell.coord.x, cell.coord.y, Color.RED)
          } else if (figure.playerNumber == 2) {
            this.drawCircle(cell.coord.x, cell.coord.y, Color.GREEN)
          } else if (figure.playerNumber == 3) {
            this.drawCircle(cell.coord.x, cell.coord.y, Color.YELLOW)
          } else if (figure.playerNumber == 4) {
            this.drawCircle(cell.coord.x, cell.coord.y, Color.BLUE)
          }
        case string: String =>
          if (string == "WALL") {
            this.drawCircle(cell.coord.x, cell.coord.y, Color.WHITE)
          } else {
            this.drawCircle(cell.coord.x, cell.coord.y, Color.BLACK)
          }
      }
    )

    controller.gameboard.cells.map(cell =>
      if (cell.possibleCells) {
        cell.contains match {
          case figure: Figure =>
            if (figure.playerNumber != controller.gameboard.playerTurn.get.number)
              this.highlightCells(cell.coord.x, cell.coord.y)
          case string: String => this.highlightCells(cell.coord.x, cell.coord.y)
        }
      }
    )
  }

  def drawCircle(x: Int, y: Int, color: Color): Boolean = {
    g2d.setColor(color)
    g2d.fillArc(x - 20, y - 20, 35, 35, 0, 360)
    repaint()
    true
  }

  def highlightCells(x: Int, y: Int): Boolean = {
    g2d.setStroke(thick)
    g2d.setColor(Color.CYAN)
    g2d.drawArc(x - 16, y - 17, 29, 30, 0, 360)
    repaint()
    true
  }

  menuBar = new MenuBar {
    contents += new Menu("Malefiz") {
      mnemonic = Key.F
      contents += new MenuItem(Action("Quit") {
        System.exit(0)
      })
    }
    contents += new Menu("Edit") {
      mnemonic = Key.E
      contents += new MenuItem(Action("Undo") {
        controller.undo()
      })
      contents += new MenuItem(Action("Redo") {
        controller.redo()
      })
      contents += new MenuItem(Action("Speichern") {
        controller.save()
      })
    }
  }

  contents = new GridBagPanel {

    background = Color.DARK_GRAY

    def constraints(
                     x: Int,
                     y: Int,
                     gridWidth: Int = 1,
                     gridHeight: Int = 1,
                     weightX: Double = 0.0,
                     weightY: Double = 0.0,
                     fill: GridBagPanel.Fill.Value = GridBagPanel.Fill.None,
                     ipadX: Int = 0,
                     ipadY: Int = 0,
                     anchor: GridBagPanel.Anchor.Value = GridBagPanel.Anchor.Center
                   ): Constraints = {
      val c = new Constraints
      c.gridx = x
      c.gridy = y
      c.gridwidth = gridWidth
      c.gridheight = gridHeight
      c.weightx = weightX
      c.weighty = weightY
      c.fill = fill
      c.ipadx = ipadX
      c.ipady = ipadY
      c.anchor = anchor
      c
    }

    add(playerLabel, constraints(0, 1, gridWidth = 2, fill = GridBagPanel.Fill.Both, ipadX = 104, ipadY = 15))
    add(playerArea, constraints(0, 2, gridWidth = 2, fill = GridBagPanel.Fill.Both))
    add(playerTurnLabel, constraints(2, 1, gridWidth = 2, fill = GridBagPanel.Fill.Both, ipadX = 104, ipadY = 15))
    add(playerTurnArea, constraints(2, 2, gridWidth = 2, fill = GridBagPanel.Fill.Both))
    add(cubeLabel, constraints(4, 1, fill = GridBagPanel.Fill.Both, ipadX = 104, ipadY = 15))
    add(cubeButton, constraints(4, 2))
    add(randomNumberLabel, constraints(6, 1, gridWidth = 2, fill = GridBagPanel.Fill.Both, ipadX = 104, ipadY = 15))
    add(randomNumberArea, constraints(6, 2, gridWidth = 2, fill = GridBagPanel.Fill.Both))
    add(informationArea, constraints(0, 3, gridWidth = 8, fill = GridBagPanel.Fill.Both, ipadY = 35))
    add(panel, constraints(0, 0, gridWidth = 9, ipadX = image.getWidth(null), ipadY = image.getHeight(null)))
  }

  listenTo(cubeButton, controller)

  reactions += {
    case ButtonClicked(`cubeButton`) =>
      controller.execute("r")
      randomNumberArea.text = controller.gameboard.dice.get.toString
      updateInformationArea()
    case gameBoardChanged: GameBoardChanged =>
      drawGameBoard()
    case winner: Winner =>
      drawGameBoard()
      Dialog.showConfirmation(
        contents.head,
        Statements.value(StatementRequest(controller)),
        optionType = Dialog.Options.Default
      )
      controller.resetGameBoard()
      playerArea.text = ""
      visible = false
      /*
      entryGui.visible = true
      
       */
  }

  size = new Dimension(900, 1100)
}