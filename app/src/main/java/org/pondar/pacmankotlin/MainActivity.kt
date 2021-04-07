package org.pondar.pacmankotlin

import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import android.view.LayoutInflater
import android.content.Context
import android.transition.TransitionManager
import android.view.Gravity
import android.widget.*



class MainActivity : AppCompatActivity() {

    //reference to the game class.
    private var game: Game? = null

    //timer class
    private var myTimer: Timer = Timer()

    var counter: Int = 0
    var direction: String? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //makes sure it always runs in portrait mode
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_main)

        myTimer.schedule(object : TimerTask() {
            override fun run() {
                timerMethod()
            }
        }, 0, 50) //call the function every 50ms

        myTimer.schedule(object : TimerTask() {
            override fun run() {
                timerMethodTwo()

            }
        }, 0, 1000) //call the function every second


        game = Game(this, pointsView, timeLeftView, gameStatusView)

        //intialize the game view clas and game class
        game?.setGameView(timerView)
        timerView.setGame(game)
        game?.newGame()

        //Pacman to the Right ClickListener
        moveRight.setOnClickListener {
            direction = "right"
        }

        //Pacman to the Left ClickListener
        moveLeft.setOnClickListener {
            direction = "left"
        }

        //Pacman Up ClickListener
        moveUp.setOnClickListener {
            direction = "up"
        }

        //pacman Down ClickListener
        moveDown.setOnClickListener {
            direction = "down"
        }

        continueGame.setOnClickListener{
            game?.newGame()
            direction = null
        }

        pauseGame.setOnClickListener{
            if (game!!.running) {
                pauseGame.text = "Resume game"
                game!!.running = false

            }
            else{
                pauseGame.text = "Pause game"
                game!!.running = true
            }

        }

        level1.setOnClickListener {
            if(game!!.level==="Level 2"){
                game?.level="Level 1"
                game?.newGame()
                direction = null
                level1.text = "Level 1"
            }



         level2.setOnClickListener {
             if(game!!.level==="Level 1"){
                 game?.level = "Level 2"
                 game?.newGame()
                 direction = null
                 level2.text = "Level 2"
             }
         }

        }

    }

    private fun timerMethod() {
        this.runOnUiThread(timerTick)
    }

    private fun timerMethodTwo() {
        this.runOnUiThread(timeLeft)
    }

    private val timeLeft= Runnable {
        if (game?.gameTime !=0 && game!!.running){
            game?.gameTime = game!!.gameTime -1
            timeLeftView.text = "${"Time left:"} ${game!!.gameTime} s"

        }else{(game?.gameTime==0 )

            game!!.running = false
            direction =null
        }
    }

    private val timerTick = Runnable {

        counter++

        if (game!!.running) {
            game?.moveEnemy(50)

            //game?.moveEnemy(10)

            if (direction == "right") {
                game?.movePacmanRight(10)

            }

            if (direction == "left") {
                game?.movePacmanLeft(10)

            }

            if (direction == "up") {
                game?.movePacmanUp(10)

            }

            if (direction == "down") {
                game?.movePacmanDown(10)

            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        if (id == R.id.action_settings) {
            Toast.makeText(this, "settings clicked", Toast.LENGTH_LONG).show()
            return true
        } else if (id == R.id.action_newGame) {
            Toast.makeText(this, "New Game clicked", Toast.LENGTH_LONG).show()
            game?.newGame()
            direction = null
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}


