package org.pondar.pacmankotlin

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ArrayAdapter
import android.media.MediaPlayer
import android.widget.TextView
import java.util.*



/**
 *
 * This class should contain all your game logic
 */

class Game(private var context: Context, view: TextView, timeView: TextView, gameStatusView: TextView) {


    private var pointsView: TextView = view

    private  var timeLeftView: TextView = timeView
    private  var gameStatusView : TextView = gameStatusView

    private var points: Int = 0

    var gameStatus: String? = null
    var running: Boolean = false
    var level: String = "Level 1"



    //bitmap of the pacman - initiate the type and coordinates
    var pacBitmap: Bitmap
    var pacx: Int = 0
    var pacy: Int = 0

    var gameTime:Int = 0


    //bitmap of the gold coin - initiate the type and coordinates
    var coinBitmap: Bitmap
    var coinx: Int = 0
    var coiny: Int = 0


    var enemyBitmap:Bitmap
    var enemyx:Int = 0
    var enemyy:Int = 0


    var pacIsmoving: Boolean = false

    //did we initialize the coins?
    var coinsInitialized = false

    //did we initialize the enemy?
    var enemiesInitialized = false

    //the list of goldcoins - initially empty
    var coins = ArrayList<GoldCoin>()


    //the list of enemies
    var enemies = ArrayList<Enemy>()


    //a reference to the gameview
    private var gameView: GameView? = null
    private var h: Int = 0
    private var w: Int = 0 //height and width of screen


    //The init code is called when we create a new Game class.
    //it's a good place to initialize our images.
    init {
        pacBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.pacman) //pacman image
        coinBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.goldcoin1) // coin image
        enemyBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.enemy1) // enemy image
    }

    //Declaring Array of coins with a number of coins
    //Declaring outside of function so we can acces it from other places
   // var coinArray: ArrayList<GoldCoin> = arrayListOf()

    fun setGameView(view: GameView) {
        this.gameView = view
        //coinsInitialized=false
    }


    //TODO initialize goldcoins also here
    fun initializeGoldcoins() {
        //DO Stuff to initialize the array list with some coins.




        //val numOfCoins = 10
        val numOfCoins = (1..10).random()
        //val numOfCoins = if (level =="Level1") 5 else 10
        //Adding coordinates to the ArrayList gold coins, using the height and width, together with the .random function to place the coins at random positions

        var corX =(coinBitmap.width..w-coinBitmap.width step coinBitmap.width)
        var corY =(coinBitmap.height..h-coinBitmap.height step coinBitmap.height)
        var avalibleCorX = corX.filterNot { it >400 && it < 400+pacBitmap.width}
        var avalibleCorY =corY.filterNot { it >400&& it < 400+pacBitmap.height }

        for (i in 1..numOfCoins) {
            val coin = GoldCoin(avalibleCorX.random(), avalibleCorY.random(), false)
            coins.add(coin)

        }



        coinsInitialized = true

    }

    //initiate enemies
    fun initializeEnemies(){
        enemies.clear()
        var enemiesAmount = if(level==="Level 1") 1 else 2 // in case the Level is set up to 1 - the game will have 1 enemy, if the game level goes to 2, the game will have 2 enemies

        var corX =(enemyBitmap.width..w-enemyBitmap.width step enemyBitmap.width)
        var corY =(enemyBitmap.height..h-enemyBitmap.height step enemyBitmap.height)
        println(corX)
        var avalibleCorX = corX.filterNot { it >400 && it < 400+pacBitmap.width}
        var avalibleCorY =corY.filterNot { it >400&& it < 400+pacBitmap.height }


        for ( i in 1..enemiesAmount) {
            val enemy = Enemy(avalibleCorX.random(), avalibleCorY.random())

            enemies.add(enemy)
        }

        enemiesInitialized = true
    }



    fun newGame() {
        pacBitmap = BitmapFactory.decodeResource(context.resources,R.drawable.pacman)
        pacx = 10
        pacy = 10  //just some starting coordinates - you can change this.
        //reset the points
        coinsInitialized = false
        enemiesInitialized = false
        //pacIsmoving = true
        points = 0
        pointsView.text = "${context.resources.getString(R.string.points)} $points"
        gameView?.invalidate() //redraw screen
        gameTime = if(level==="Level 1") 60 else 40
        timeLeftView.text = "${"Time left:"} $gameTime s"
        running =true
        gameStatus = "in process"
        gameStatusView.text = "Game $gameStatus"
        coins.clear()
        enemies.clear()

    }

    fun setSize(h: Int, w: Int) {
        this.h = h
        this.w = w
    }

    //fun doCollisionCheck()
    //Move Pacman to the Right
    fun movePacmanRight(pixels: Int) {
        //with boundaries included
        //pacman is moving to the right, getting + values on the x axis, until the new posibion is smaller than w
        if (pacx + pixels + pacBitmap.width < w) {
            pacx = pacx + pixels
            gameView!!.invalidate()
            doCollisionCheck()

        }
    }

    // Move Pacman to the left
    fun movePacmanLeft(pixels: Int) {
        //with boundaries included
        //pacman is moving the the left, getting - values on the x axis and then stops before the new value of the movement is bigger than 0
        if (pacx - pixels > 0) {
            pacx = pacx - pixels
            doCollisionCheck()
            gameView!!.invalidate()
        }
        //change photo of pacman considering the button orientation that the user is clicking
        //if (pacIsmoving) pacBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.pacman)
    }

    //Move Pacman Up
    fun movePacmanUp(pixels: Int) {
        //with boundaries included
        //pacman is moving up, getting - values on the y axis, until the new movement has a value bigger than 0 on the y axis
        if (pacy - pixels > 0) {
            pacy = pacy - pixels
            doCollisionCheck()
            gameView!!.invalidate()
        }

        //change photo of pacman considering the button orientation that the user is clicking
        //if (pacIsmoving) pacBitmap = BitmapFactory.decodeResource(context.resources,R.drawable.pacman)

    }

    //Move Pacman Down
    fun movePacmanDown(pixels: Int) {
        //with boundaries included
        //pacman is moving down, getting + values on the y axis, until the new movement reaches a position with a value smaller than the canvas height
        if (pacy + pixels + pacBitmap.height < h) {
            pacy = pacy + pixels
            doCollisionCheck()
            gameView!!.invalidate()
        }
    }
        //change photo of pacman considering the button orientation that the user is clicking
        //if (pacIsmoving) pacBitmap = BitmapFactory.decodeResource(context.resources,R.drawable.pacman)

    fun moveEnemy (pixels:Int){

        for (enemy in enemies) {
            val direction = (1..4).random()

            if(direction == 1 &&enemy.enemyX + pixels + enemyBitmap.width < w){
                enemy.enemyX = enemy.enemyX + pixels
                gameView!!.invalidate()
                doCollisionCheck()
            }
            if(direction == 2 &&enemy.enemyX > 20){
                println(enemy.enemyX)
                enemy.enemyX =enemy.enemyX - pixels
                gameView!!.invalidate()
                doCollisionCheck()
            }
            if(direction == 3 &&enemy.enemyY > 10){
                enemy.enemyY =enemy.enemyY - pixels
                gameView!!.invalidate()
                doCollisionCheck()
            }
            if(direction == 4 &&enemy.enemyY + pixels + enemyBitmap.height < h){
                enemy.enemyY =enemy.enemyY + pixels
                gameView!!.invalidate()
                doCollisionCheck()
            }
        }

    }




    //create a function that checks if there is a collision between pacman and a gold coin
    //fun pacmanCoinCollision() {
     //   println("We have detected a collision")
    //}


    //TODO check if the pacman touches a gold coin
    //and if yes, then update the neccesseary data
    //for the gold coins and the points
    //so you need to go through the arraylist of goldcoins and
    //check each of them for a collision with the pacman

    fun getDistance(corX:Int, corY:Int, objectBitmap: Bitmap ):Double{
        val offsetObject = objectBitmap.width/2
        val offsetPacman = pacBitmap.width/2
        val deltaX = ((corX+offsetObject)-(pacx+offsetPacman)  ).toDouble()
        val deltaY = ((corY+offsetObject)-(pacy+offsetPacman)  ).toDouble()
        var calcDistance = Math.sqrt((deltaX*deltaX) +(deltaY*deltaY))
        return calcDistance
    }

    fun doCollisionCheck() {
        //fun distance(x1:Int,y1:Int,x2:Int,y2:Int) : Float {
        // calculate distance and return it
        //}
        //var coinsTaken = 0
        for (coin in coins) {
            var distance = getDistance(coin.coinx, coin.coiny, coinBitmap)
            if (distance < 130) {
                coin.taken = true

            }
        }
        var coinsTaken = coins.filter{it.taken ==true}

            points = coinsTaken.size
            pointsView.text = "${ "points: " }$points "
            if (coinsTaken.size == coins.size){
                gameStatus = "Won"
                gameStatusView.text = "Game $gameStatus"
                running = false

            }




        for (enemy in enemies) {
            var distance = getDistance(enemy.enemyX, enemy.enemyY, enemyBitmap)
            println("enemy " + enemy.enemyX + " " + enemy.enemyY)
            if (distance < 160) {
                gameStatus = "Lost"
                gameStatusView.text = "Game $gameStatus"
                running = false
            }
        }


        //enemies.forEach{
        //    var distance = getDistance(it.enemyX, it.enemyY, enemyBitmap)
        //    println("enemy "+it.enemyX+" "+it.enemyY)
        //    if(distance<160){
        //        gameStatus = "Lost"

        //    }
        //}


    }

    }

