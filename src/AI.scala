import java.util.List
import AI._

import scala.collection.mutable.ListBuffer

//remove if not needed
import scala.collection.JavaConversions._

object AI {

  def createGameTree(s: State, d: Int): Unit = {
    if(d == 0) return
    s.initializeChildren()
    val children = s.getChildren
    for(child <- children)
      createGameTree(child, d-1)
  }

  def minimax(ai: AI, s: State) {
    ai.minimax(s)
  }
}

class AI(private var player: Player, private var depth: Int) extends Solver {

  override def getMoves(b: Board): Array[Move] = {
    var state = new State(player.opponent, b, null)
    createGameTree(state, depth)
    minimax(state)
    Array[Move](state.getChildren.max.getLastMove)
  }

  def minimax(s: State): Unit = {
    if (s.getChildren.length == 0) {
      s.setValue(evaluateBoard(s.getBoard))
    } else {
      for (child <- s.getChildren) {
        minimax(child)
        if (s.player == this.player) {
          s.setValue(s.getChildren.min.getValue)
        } else {
          s.setValue(s.getChildren.max.getValue)
        }
      }
    }
  }

  def evaluateBoard(b: Board): Int = {
    val winner = b.hasConnectFour()
    var value = 0
    if (winner == null) {
      val locs = b.winLocations()
      for (loc <- locs; p <- loc) {
        value += (if (p == player) 1 else if (p != null) -1 else 0)
      }
    } else {
      var numEmpty = 0
      var r = 0
      while (r < Board.NUM_ROWS) {
        var c = 0
        while (c < Board.NUM_COLS) {
          if (b.getTile(r, c) == null) numEmpty += 1
          c = c + 1
        }
        r = r + 1
      }
      value = (if (winner == player) 1 else -1) * 10000 * numEmpty
    }
    value
  }
}

