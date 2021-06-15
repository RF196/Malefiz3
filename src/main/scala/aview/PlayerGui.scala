package aview

import java.awt.{Color, Font}

import controller.ControllerInterface
import javax.swing.SpringLayout.Constraints
import scala.swing.event.ButtonClicked
import scala.swing.{Action, Button, Dimension, Frame, GridBagPanel, Label, Menu, MenuBar, MenuItem, TextField}

class PlayerGui (controller: ControllerInterface) extends Frame {
  
  title = "Malefiz"
  visible = false
  centerOnScreen()

  val playerLabel: Label = new Label("Enter the names")
  playerLabel.foreground = Color.WHITE
  playerLabel.font = new Font("Sans Serif", Font.BOLD, 22)

  val playerOneLabel: Label = new Label("Player 1   ")
  playerOneLabel.foreground = Color.RED
  playerOneLabel.font = new Font("Sans Serif", Font.ITALIC, 20)
  val playerOneName: TextField = new TextField {columns = 15}

  val playerTwoLabel: Label = new Label("Player 2   ")
  playerTwoLabel.foreground = Color.GREEN
  playerTwoLabel.font = new Font("Sans Serif", Font.ITALIC, 20)
  val playerTwoName: TextField = new TextField {columns = 15}

  val playerThreeLabel: Label = new Label("Player 3   ")
  playerThreeLabel.foreground = Color.ORANGE
  playerThreeLabel.font = new Font("Sans Serif", Font.ITALIC, 20)
  val playerThreeName: TextField = new TextField {columns = 15}

  val playerFourLabel: Label = new Label("Player 4    ")
  playerFourLabel.foreground = Color.BLUE
  playerFourLabel.font = new Font("Sans Serif", Font.ITALIC, 20)
  val playerFourName: TextField = new TextField {columns = 15}

  val continueButton = new Button("start new game")

  menuBar = new MenuBar{
    contents += new Menu("Malefiz") {
      contents += new MenuItem(Action("Quit") {
        System.exit(0)
      })
    }
  }

  contents = new GridBagPanel {

    background = Color.DARK_GRAY

    def constraints(x: Int,
                    y: Int,
                    gridWidth: Int = 1,
                    gridHeight: Int = 1,
                    weightX: Double = 0.0,
                    weighty: Double = 0.0,
                    ipadX: Int = 0,
                    ipadY: Int = 0,
                    fill: GridBagPanel.Fill.Value = GridBagPanel.Fill.None,
                    anchor: GridBagPanel.Anchor.Value = GridBagPanel.Anchor.Center)
    : Constraints = {
      val c = new Constraints
      c.gridx = x
      c.gridy = y
      c.gridwidth = gridWidth
      c.gridheight = gridHeight
      c.weightx = weightX
      c.weighty = weighty
      c.fill = fill
      c.ipadx = ipadX
      c.ipady = ipadY
      c.anchor = anchor
      c
    }

    add(playerLabel,
      constraints(0, 0, gridWidth = 2, ipadY = 20, fill=GridBagPanel.Fill.Horizontal, anchor = GridBagPanel.Anchor.PageStart))
    add(playerOneLabel,
      constraints(0, 1, ipadY = 20))
    add(playerOneName,
      constraints(1, 1, ipadY = 20))
    add(playerTwoLabel,
      constraints(0, 2, ipadY = 20))
    add(playerTwoName,
      constraints(1, 2, ipadY = 20))
    add(playerThreeLabel,
      constraints(0, 3, ipadY = 20))
    add(playerThreeName,
      constraints(1, 3, ipadY = 20))
    add(playerFourLabel,
      constraints(0, 4, ipadY = 20))
    add(playerFourName,
      constraints(1, 4, ipadY = 20))
    add(continueButton,
      constraints(0, 8, gridWidth = 2, ipadY = 20, anchor = GridBagPanel.Anchor.South))
  }

  listenTo(continueButton)

  reactions += {
    case ButtonClicked(`continueButton`) =>
      val pList = List(playerOneName.text, playerTwoName.text, playerThreeName.text, playerFourName.text)
      pList.indices.foreach(x => if(pList(x) != "") controller.execute("n " + pList(x)))
      controller.execute("start")
      this.visible = false
      
      /*
      gameGui.visible = true
      gameGui.updatePlayerArea()
      gameGui.updatePlayerTurn()
      gameGui.drawGameBoard()
      gameGui.updateInformationArea()
      
       */
  }

  size = new Dimension(500, 500)
}
