package com.awp.handspeed

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.PersistableBundle
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    private lateinit var gameScoreText: TextView
    private lateinit var timeLeftText: TextView
    private lateinit var tapMeImg: ImageView
    private var score=0
    private var gameStarted = false
    private lateinit var countDownTimer: CountDownTimer
    private var initialCountDown: Long = 60000
    private var countDownInterval: Long = 1000
    private var timeLeft=60

    //debugging
    private val TAG=MainActivity::class.java.simpleName

    companion object{
        private const val SCORE_KEY="SCORE_KEY"
        private const val TIME_LEFT_KEY="TIME_LEFT_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        gameScoreText=findViewById(R.id.gameScoreText)
        timeLeftText=findViewById(R.id.timeLeftText)
        tapMeImg=findViewById(R.id.tapMeImg)

        tapMeImg.setOnClickListener { v->
            val bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce);
            v.startAnimation(bounceAnimation)
            incrementScore()
        }

        Log.d(TAG, "onCreat called. Score is: $score")

        if(savedInstanceState != null) {
            score=savedInstanceState.getInt(SCORE_KEY)
            timeLeft=savedInstanceState.getInt(TIME_LEFT_KEY)
            restoreGame()
        } else {
            resetGame()
        }
    }

    private fun restoreGame() {
        val restoredScore=getString(R.string.yourScore, score)
        gameScoreText.text=restoredScore
        val restoredTime=getString(R.string.timeLeft, timeLeft)
        timeLeftText.text=restoredTime
        countDownTimer=object : CountDownTimer((timeLeft*1000).toLong(),countDownInterval){
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = millisUntilFinished.toInt() / 1000
                val timeLeftString = getString(R.string.timeLeft, timeLeft)
                timeLeftText.text = timeLeftString
            }

            override fun onFinish() {
                endGame()
            }
        }

        countDownTimer.start()
        gameStarted=true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SCORE_KEY, score)
        outState.putInt(TIME_LEFT_KEY, timeLeft)
        countDownTimer.cancel()
        Log.d(TAG, "onSaveInstanceState: Saving Score: $score & Time Left: $timeLeft")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy called")
    }

    private fun incrementScore() {
        score++
        val newScore = getString(R.string.yourScore, score)
        gameScoreText.text=newScore
        if(!gameStarted){
            startGame()
        }
    }

    private fun resetGame() {
        score=0

        val initialScore = getString(R.string.yourScore, score)
        gameScoreText.text=initialScore

        val initialTimeLeft = getString(R.string.timeLeft, 60)
        timeLeftText.text=initialTimeLeft

        countDownTimer=object : CountDownTimer(initialCountDown, countDownInterval){
            override fun onTick(millisUntilFinished: Long) {
                timeLeft=millisUntilFinished.toInt()/1000
                val timeLeftString=getString(R.string.timeLeft, timeLeft)
                timeLeftText.text=timeLeftString
            }

            override fun onFinish() {
                endGame()
            }
        }
        gameStarted=false

    }

    private fun startGame() {
        countDownTimer.start()
        gameStarted=true
    }

    private fun endGame() {
        Toast.makeText(this, getString(R.string.gameOverMsg, score), Toast.LENGTH_LONG).show()
        resetGame()
    }


}