package com.example.dicegame

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.SwitchCompat
import kotlin.random.Random


class NewGame : AppCompatActivity() {

    private var playerRollCount = 0                             // Create a variable to track the player roll count
    private var computerRollCount = 0                           // Create a variable to track the computer roll count
    private var playerScore = 0                                 // Create a variable to track the player total score
    private var computerScore = 0                               // Create a variable to track the computer total score
    private val playerKeep = BooleanArray(5) {false}        //Create a 5-element BooleanArray named "playerKeep" and initialize each element to "false"
    private var computerReroll: Boolean = true                  // Create a Boolean variable named "computerReroll" and set its initial value to "true"
    private var playerDiceTotal =0                              // Create a variable to track of the player Dice Total
    private var computerDiceTotal =0                            // Create a variable to track of the computer Dice Total
    private var dicesP = IntArray(5)                        // integer array of length 5 to add player dice
    private var dicesC = IntArray(5)                        // integer array of length 5 to add computer dice
    private var winningScore: Int? = null                        // Create a variable to track the target winning score
    private var hardMode = false

    // Define a companion object inside the class
    companion object {
        var currentComputerPoint: Int = 0  //create a mutable variable for computer win count
        var currentHumanPoint: Int = 0
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_game)

        var intent = intent
        winningScore = intent.getIntExtra("target",101) //get the target value from popup

        hardMode = intent.getBooleanExtra("hard",false) ////get the target value from popup

        displayScores()

        // Find the ImageViews with the ID's in the layout and assign the reference to diceImages

        val diceImage01 =  findViewById<ImageView>(R.id.player_dice1)
        val diceImage02 =  findViewById<ImageView>(R.id.player_dice2)
        val diceImage03 =  findViewById<ImageView>(R.id.player_dice3)
        val diceImage04 =  findViewById<ImageView>(R.id.player_dice4)
        val diceImage05 =  findViewById<ImageView>(R.id.player_dice5)
        val diceImage06 =  findViewById<ImageView>(R.id.computer_dice1)
        val diceImage07 =  findViewById<ImageView>(R.id.computer_dice2)
        val diceImage08 =  findViewById<ImageView>(R.id.computer_dice3)
        val diceImage09 =  findViewById<ImageView>(R.id.computer_dice4)
        val diceImage10 =  findViewById<ImageView>(R.id.computer_dice5)

        // Find the textview with the ID's in the layout and assign the reference to text
        val computerTotScore = findViewById<TextView>(R.id.computerTotScore)

        // player roll count find the textview with the ID's in the layout and assign the reference to text
        val rollCount = findViewById<TextView>(R.id.rollCount)

        // Find the buttons with the ID's in the layout and assign the reference to buttons
        val rollButton: Button = findViewById(R.id.throw_button)
        val scoreButton: Button = findViewById(R.id.score_button)

        // Create an array called "diceImagesP" with references to the five ImageView objects.
        val diceImagesP = arrayOf(diceImage01, diceImage02, diceImage03, diceImage04, diceImage05)

        //target score text view
        val targetScoreText = findViewById<TextView>(R.id.targetScoreTxt)
        targetScoreText.text = "TARGET:$winningScore"


        // Set click listeners for dice ImageViews. to keep the dices that player clicks
        for (i in diceImagesP.indices) {

            diceImagesP[i].setOnClickListener {
                if(playerRollCount!=0){
                    playerKeep[i] = !playerKeep[i] //
                    it.alpha = if (playerKeep[i]) 0.5f else 1f // Change alpha to indicate selection
                }
            }

        }

        // Set click listeners for roll button
        rollButton.setOnClickListener {

            //checks the player roll count
            if (playerRollCount  ==2) {


                rollDiceP(diceImage01, diceImage02, diceImage03, diceImage04, diceImage05)
                rollDiceC(diceImage06, diceImage07, diceImage08, diceImage09, diceImage10, computerTotScore)

                playerScore += playerDiceTotal
                computerScore += computerDiceTotal
                playerRollCount = 0
                computerRollCount = 0
                updateScoresP()
                updateScoresC()

                // Update the alpha value of the corresponding ImageView object
                for(i in diceImagesP.indices){
                    playerKeep[i]=false
                    diceImagesP[i].alpha=1f
                }


            } else{
                rollDiceP(diceImage01, diceImage02, diceImage03, diceImage04, diceImage05)
                rollDiceC(diceImage06, diceImage07, diceImage08, diceImage09, diceImage10, computerTotScore)

                computerRollCount+=1

            }
            rollCount.text = "ROLL: $playerRollCount"      //display the roll count
            checkForWinner()
        }

        // Set click listeners for roll button
        scoreButton.setOnClickListener {

            if(playerRollCount >= 1){

                rerollComputer(diceImage06, diceImage07, diceImage08, diceImage09, diceImage10, computerTotScore)

                playerRollCount=0
                computerRollCount=0

                playerScore += playerDiceTotal
                computerScore += computerDiceTotal

                // Update the alpha value of the corresponding ImageView object
                for(i in diceImagesP.indices){
                    playerKeep[i]=false
                    diceImagesP[i].alpha=1f
                }

                updateScoresP()
                updateScoresC()
            }
            checkForWinner()
        }

    }


    //function for player dice
    private fun rollDiceP(diceImage01: ImageView, diceImage02: ImageView, diceImage03:ImageView,  diceImage04:ImageView,  diceImage05:ImageView) {

        val diceImages = arrayOf(diceImage01, diceImage02, diceImage03, diceImage04, diceImage05)

        // Create a random number between 1 and 6 (inclusive) and place it in the dicesP array at the current index i.
        for (i in diceImages.indices) {
            if (!playerKeep[i]) {           // Only roll unselected dice
                val diceRoll = Random.nextInt(1, 7)
                dicesP[i] = diceRoll

                val drawableResource = when (diceRoll) {
                    1 -> R.drawable.diceone
                    2 -> R.drawable.dicetwo
                    3 -> R.drawable.dicethree
                    4 -> R.drawable.dicefour
                    5 -> R.drawable.dicefive
                    else -> R.drawable.dicesix
                }
                diceImages[i].setImageResource(drawableResource)
            }
        }

        //assign this array list value to the playerDiceTotal variable
        playerDiceTotal = dicesP.sum()

        // Increment the roll count
        playerRollCount++


        for (i in playerKeep.indices) {
            playerKeep[i] = false
            diceImages[i].alpha = 1f
        }

    }


    /**TThere are two essential aspects to consider in this strategy: when to re-roll the dice and which dice to re-roll.
     The guiding theory behind the re-rolling choice is to replace dice with values of one, two, or three while retaining
     those with values of four, five, or six. This strategy seeks to optimize the machine player's ultimate score,
     thereby providing a more difficult and interesting experience for the human opponent. The computer player successfully
     improves its odds of obtaining a higher score by meticulously evaluating each die's value and selectively
     re-rolling low-value dice, resulting in a more competitive and exciting game for both players.  **/

    private fun rollDiceC(diceImage06: ImageView, diceImage07: ImageView, diceImage08: ImageView, diceImage09: ImageView, diceImage10: ImageView, computerTotScore: TextView) {
        val diceImages = arrayOf(diceImage06, diceImage07, diceImage08, diceImage09, diceImage10)
        val computerKeep = BooleanArray(5) { false }

        //computer random strategy
        if (computerRollCount >= 1) {

            if(!hardMode){  //if user select the easy mode
                for (i in computerKeep.indices) {
                    computerKeep[i] = Random.nextBoolean()
                }
            }else{      //if user select the hard mode
                for (i in computerKeep.indices) {

                    if (dicesC[i] == 4 || dicesC[i] == 5 || dicesC[i] == 6){     //if dice value is 4,5,6 dice are not rolling
                        computerKeep[i] = true
                    }

                }
            }
        }


        // Create a random number between 1 and 6 (inclusive) and place it in the dicesC array at the current index i.
        for (i in diceImages.indices) {
            if (!computerKeep[i]) { // Only roll unselected dice
                val diceRoll = Random.nextInt(1, 7)
                dicesC[i] = diceRoll
                val drawableResource = when (diceRoll) {
                    1 -> R.drawable.diceone
                    2 -> R.drawable.dicetwo
                    3 -> R.drawable.dicethree
                    4 -> R.drawable.dicefour
                    5 -> R.drawable.dicefive
                    else -> R.drawable.dicesix
                }
                diceImages[i].setImageResource(drawableResource)
            }
        }

        //assign this array list value to the computerDiceTotal variable
        computerDiceTotal = dicesC.sum()


//        computerScore += diceRollSum // update the total score

        // Reset computerKeep array and computerRollCount after the third roll
        for (i in computerKeep.indices) {
            computerKeep[i] = false
        }

    }

    //computer reroll
    private fun rerollComputer(diceImage06: ImageView, diceImage07: ImageView, diceImage08: ImageView, diceImage09: ImageView, diceImage10: ImageView, computerTotScore: TextView){

        //check the computer roll count
        for (i in (computerRollCount+1)..3) {

            // Update reroll decision
            computerReroll = Random.nextBoolean()

            //if not rolling
            if (!computerReroll) {
                break
            }

            rollDiceC(diceImage06, diceImage07, diceImage08, diceImage09, diceImage10, computerTotScore)
        }
    }

    //to display the updated score
    private fun updateScoresP() {
        val playerTotScore = findViewById<TextView>(R.id.playerTotScore)        //displayed on the top right of the screen.
        playerTotScore.text = "PLAYER: $playerScore"
    }

    //to display the updated score
    private fun updateScoresC() {
        val computerTotScore = findViewById<TextView>(R.id.computerTotScore)    //displayed on the top right of the screen.
        computerTotScore.text = "COMPUTER: $computerScore"
    }



    private fun checkForWinner() {

        //default value for winningScore
        val targetScore = winningScore ?: 101

        // Inflate the popup_layout.xml file
        if (playerScore >= targetScore) {
            if (computerScore >= targetScore) {
                if (playerScore > computerScore) {
                    storeWins(0,1)
                    winPopup()
                }else if (playerScore == computerScore){
                    tiePop()
                    playerRollCount=3
                    computerRollCount=3
                   // rollNumber.text = "Tie-Breaker Roll"
                }else {
                    storeWins(1,0)
                    losePopup()
                }
            } else {
                storeWins(0,1)
                winPopup()
            }
        } else if (computerScore >= targetScore) {
            computerScore++
            storeWins(1,0)
            losePopup()
            }



    }

    private fun winPopup() {
        val popupView = layoutInflater.inflate(R.layout.win_pop, null)

        // Move the findViewById() call after inflating the popupView
        val closeButtonW: Button = popupView.findViewById(R.id.cancel_buttonW)

        // Create a PopupWindow object
        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )
        //popupWindow.isOutsideTouchable = false
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0)

        closeButtonW.setOnClickListener {
            // Dismiss the popup window
            popupWindow.dismiss()
            endTurn()
        }
    }



    private fun losePopup() {
        val popupView = layoutInflater.inflate(R.layout.lose_pop, null)

        // Move the findViewById() call after inflating the popupView
        val closeButtonL: Button = popupView.findViewById(R.id.cancel_buttonL)

        // Create a PopupWindow object
        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )
       // popupWindow.isOutsideTouchable = false
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0)

        closeButtonL.setOnClickListener {
            // Dismiss the popup window
            popupWindow.dismiss()
            endTurn()
        }
    }

    private fun tiePop() {
        val popupView = layoutInflater.inflate(R.layout.win_pop, null)

        // Move the findViewById() call after inflating the popupView
        val closeButtonW: Button = popupView.findViewById(R.id.cancel_buttonW)

        // Create a PopupWindow object
        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0)

        closeButtonW.setOnClickListener {
            // Dismiss the popup window
            popupWindow.dismiss()
            endTurn()
        }
    }

    //when someone wins score will updated to 0
    private fun endTurn() {

        playerScore = 0
        computerScore = 0

        updateScoresP()
        updateScoresC()
    }

    //store the win count
    private fun storeWins(computerPoint: Int, playerPoint: Int) {
        currentComputerPoint += computerPoint
        currentHumanPoint += playerPoint

        displayScores()
    }

    //update win count
    private fun displayScores() {
        findViewById<TextView>(R.id.player_wins).text = "PLAYER: $currentHumanPoint"
        findViewById<TextView>(R.id.computer_wins).text = "COMPUTER: $currentComputerPoint"

    }

}
