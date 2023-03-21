package com.example.dicegame

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupWindow
import android.widget.ToggleButton
import androidx.appcompat.widget.SwitchCompat
import com.google.android.material.textfield.TextInputEditText

class IntroPage : AppCompatActivity() {

    private lateinit var  newGameBtn:Button
    private lateinit var aboutBtn:Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro_page)


        newGameBtn= findViewById(R.id.new_game_btn)
        aboutBtn= findViewById(R.id.about_btn)

        val aboutPopup = findViewById<Button>(R.id.about_btn)

        newGameBtn.setOnClickListener {
            showChangeScorePopup()
        }

        aboutPopup.setOnClickListener {
            showAboutPopup()
        }


    }

    private fun showAboutPopup() {
        // Inflate the popup_layout.xml file
        val popupView = layoutInflater.inflate(R.layout.about, null)

        // Create a PopupWindow object
        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        val closeButton = popupView.findViewById<Button>(R.id.cancel_about)

        // Set a click listener for the close button
        closeButton.setOnClickListener {
            // Dismiss the popup window
            popupWindow.dismiss()
        }

        // Show the popup window
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0)
    }

    @SuppressLint("MissingInflatedId", "UseCompatLoadingForDrawables")
    private fun showChangeScorePopup() {
        // Inflate the popup_layout.xml file
        val scorePopupView = layoutInflater.inflate(R.layout.score_change, null)



        // Create a PopupWindow object
        val scorePopupWindow = PopupWindow(
            scorePopupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        // Get references to the views in the popup window
        val scoreText = scorePopupView.findViewById<TextInputEditText>(R.id.current_game_score)
        val closeButton = scorePopupView.findViewById<Button>(R.id.cancel_button)
        val easyButton = scorePopupView.findViewById<Button>(R.id.easyBtn)
        val hardButton = scorePopupView.findViewById<Button>(R.id.hardBtn)


        // Set a click listener for the close button
        closeButton.setOnClickListener {
            // Dismiss the popup window
            scorePopupWindow.dismiss()
        }

        easyButton.setOnClickListener {

            var hard = false

            var targetScore1 = scoreText.text.toString()
            val targetScore:Int?=targetScore1.toInt()

            //winScore.game = targetScore
            val intent = Intent(this, NewGame::class.java)
            intent.putExtra("hard",hard)
            intent.putExtra("target",targetScore)
            startActivity(intent)

            scorePopupWindow.dismiss()


        }

        hardButton.setOnClickListener {

            var hard = true

            var targetScore1 = scoreText.text.toString()
            val targetScore:Int?=targetScore1.toInt()

            //winScore.game = targetScore
            val intent = Intent(this, NewGame::class.java)
            intent.putExtra("hard",hard)
            intent.putExtra("target",targetScore)
            startActivity(intent)

            scorePopupWindow.dismiss()


        }

        // Show the popup window
        scorePopupWindow.showAtLocation(scorePopupView, Gravity.CENTER, 0, 0)
        }
}






