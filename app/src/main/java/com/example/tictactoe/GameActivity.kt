package com.example.tictactoe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.tictactoe.Model.GameData
import com.example.tictactoe.Model.GameModel
import com.example.tictactoe.Model.GameStatus
import com.example.tictactoe.databinding.ActivityGameBinding

class GameActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityGameBinding

    private var gameModel: GameModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set click listeners for game buttons
        binding.btn0.setOnClickListener(this)
        binding.btn1.setOnClickListener(this)
        binding.btn2.setOnClickListener(this)
        binding.btn3.setOnClickListener(this)
        binding.btn4.setOnClickListener(this)
        binding.btn5.setOnClickListener(this)
        binding.btn6.setOnClickListener(this)
        binding.btn7.setOnClickListener(this)
        binding.btn8.setOnClickListener(this)

        // Set click listener for the start game button
        binding.startGameButton.setOnClickListener {
            startGame()
        }

        // Observe game model changes
        GameData.gameModel.observe(this) {
            gameModel = it
            setUI()
        }
    }

    private fun setUI() {
        gameModel?.apply {
            // Update button texts
            binding.btn0.text = filledPos[0]
            binding.btn1.text = filledPos[1]
            binding.btn2.text = filledPos[2]
            binding.btn3.text = filledPos[3]
            binding.btn4.text = filledPos[4]
            binding.btn5.text = filledPos[5]
            binding.btn6.text = filledPos[6]
            binding.btn7.text = filledPos[7]
            binding.btn8.text = filledPos[8]

            // Update game status text and button visibility
            binding.startGameButton.visibility = View.VISIBLE

            binding.gameStatus.text = when (gameStatus) {
                GameStatus.CREATED -> {
                    binding.startGameButton.visibility = View.INVISIBLE
                    "Game ID : $gameId"
                }
                GameStatus.JOINED -> {
                    "Click On Start Game"
                }
                GameStatus.INPROGRESS -> {
                    binding.startGameButton.visibility = View.INVISIBLE
                    "$currentPlayer's turn"
                }
                GameStatus.FINISHED -> {
                    if (winner.isNotEmpty()) {
                        "$winner 👑 WON"
                    } else {
                        "DRAW"
                    }
                }
            }
        }
    }

    private fun startGame() {
        gameModel?.apply {
            // Reset the filled positions and game status
            filledPos = mutableListOf("", "", "", "", "", "", "", "", "")
            gameStatus = GameStatus.INPROGRESS
            winner = ""
            updateGameData(this)
            setUI()
        }
    }

    private fun updateGameData(model: GameModel) {
        GameData.saveGameModel(model)
    }

    private fun checkForWinner(): Boolean {
        val winningPos = arrayOf(
            intArrayOf(0, 1, 2),
            intArrayOf(3, 4, 5),
            intArrayOf(6, 7, 8),
            intArrayOf(0, 3, 6),
            intArrayOf(1, 4, 7),
            intArrayOf(2, 5, 8),
            intArrayOf(0, 4, 8),
            intArrayOf(2, 4, 6)
        )

        gameModel?.apply {
            for (i in winningPos) {
                if (filledPos[i[0]] == filledPos[i[1]] &&
                    filledPos[i[1]] == filledPos[i[2]] &&
                    filledPos[i[0]].isNotEmpty()
                ) {
                    gameStatus = GameStatus.FINISHED
                    winner = filledPos[i[0]]
                    updateGameData(this)
                    return true
                }
            }

            // Check for draw
            if (filledPos.all { it.isNotEmpty() }) {
                gameStatus = GameStatus.FINISHED
                winner = ""
                updateGameData(this)
                return true
            }
        }
        return false
    }

    override fun onClick(v: View?) {
        gameModel?.apply {
            if (gameStatus != GameStatus.INPROGRESS) {
                Toast.makeText(applicationContext, "Game not Started.", Toast.LENGTH_SHORT).show()
                return
            }

            // Game is in progress
            val clickedPos = (v?.tag as String).toInt()
            if (filledPos[clickedPos].isEmpty()) {
                filledPos[clickedPos] = currentPlayer
                if (checkForWinner()) {
                    setUI()
                    return
                }
                currentPlayer = if (currentPlayer == "X") {
                    "O"
                } else {
                    "X"
                }
                updateGameData(this)
                setUI()
            }
        }
    }
}
