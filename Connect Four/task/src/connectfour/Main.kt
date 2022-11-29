package connectfour

import  java.io.File
import java.io.IOException
import java.util.Collections.max
import java.util.Collections.min
import kotlin.math.max
import kotlin.math.min


fun main(args: Array<String>) {
    var rows = 6
    var columns = 7
    class User {
        var name: String = "Unknown"
        var score = 0
    }

    println("Connect Four")
    println("First player's name:")
    val user1 = User()
    user1.name = readln()!!
    user1.score = 0

    println("Second player's name:")
    val user2 = User()
    user2.name = readln()!!
    user2.score = 0

    var users = listOf(user1, user2)

    var theEnd = false

    while (!theEnd) {
        println("Set the board dimensions (Rows x Columns)\n" +
                "Press Enter for default (6 x 7)")

        val str = readln()!!
        if (str != "") {
            var reg = "\\s*[0-9]+\\s*(X|x)\\s*[0-9]+\\s*".toRegex()
            if (str.matches(reg)) {
                rows = str.trim()[0].digitToInt()
                columns = str.trim()[str.trim().lastIndex].digitToInt()
                if (rows !in 5..9) {
                    println("Board rows should be from 5 to 9")
                    continue
                } else if (columns !in 5..9) {
                    println("Board columns should be from 5 to 9")
                    continue
                } else {
                    theEnd = true
                    continue
                }
            } else println("Invalid input")
        } else theEnd = true
    }

// создаем класс игра и переменную игра
class  Game {
    var user1 = user1
    var user2 = user2
    var gameStep = 1
    var countGame = 0
    var stepUser = 2
}
    var game = Game()

// начинаем цикл для ввода количество игр
    theEnd = false
    var gameString = ""
    //var countGame = 0
    while (!theEnd) {
        println(
            "Do you want to play single or multiple games?\n" +
                    "For a single game, input 1 or press Enter\n" +
                    "Input a number of games:"
        )
        var countGameStr = readln()

        if (countGameStr != "") {
            if (isDig(countGameStr) && countGameStr != "0") {
                game.countGame = countGameStr.toInt()
                if (game.countGame == 1) gameString = "Single game" else
                    gameString = "Total ${game.countGame} games"

                theEnd = true
                continue
            } else {
                println("Invalid input")
                continue
            }
        } else {
            game.countGame = 1
            gameString = "Single game"
            theEnd = true
            continue
        }
    }

// распечатка начала игры
    println("${user1.name} VS ${user2.name}")
    println("$rows X $columns board")
    println(gameString)
    if (game.countGame != 1) println("Game #${game.gameStep}")


    //инициаизируем  доску пустыми строками
    var board = MutableList(rows) { MutableList(columns) {' '} }
    printBoard(board)

    while (game.countGame + 1 != game.gameStep) {

        theEnd = false
        while (!theEnd) {

            // определяем чей шаг
            if (game.stepUser % 2 == 0)
            println("${game.user1.name}'s turn:") else
                println("${game.user2.name}'s turn:")
            var input = readln()
            if (input == "end") {
                println("Game over!")
                theEnd = true
                return
            }
            if (!isDig(input)) {
                println("Incorrect column number")
                continue
            }
            if (!isRang(input, columns)) {
                println("The column number is out of range (1 - $columns)")
                continue
            }

            //добавляем элемент на доску
            var character = if (game.stepUser % 2 == 0) 'o' else '*'
            var col = input.toInt() - 1

            var freePlace = fountFreePlace(board, col)

            if (freePlace == board.size) {
                println("Column ${col + 1} is full")
                continue
            } else {
                board[freePlace][col] = character

            }

            printBoard(board)

            // проверка на выигрыш
            if (checkWinVertical(board) || checkWinGorizont(board) || checkWinDiagonal(board)) {
                //if (checkWinDiagonal(board)){
                if (game.gameStep < game.countGame) {
                    if (game.stepUser % 2 == 0)
                        println("Player ${game.user1.name} won") else println("Player ${game.user2.name} won")
                }
                    if (game.stepUser % 2 == 0) game.user1.score += 2 else game.user2.score += 2

                    cleanBord(board)
                    // printBoard(board)
                if (game.gameStep < game.countGame) {
                    println("Score")
                    println("${game.user1.name}: ${game.user1.score} ${game.user2.name}: ${game.user2.score}")
                }


                game.gameStep += 1
                game.stepUser += 1
                    break

            }
            //проверка на заполненность всей доски
            if (checkEmptyBoard(board)) {
                if (game.gameStep < game.countGame) println("It is a draw")
                game.user1.score += 1
                game.user2.score += 1

                cleanBord(board)
               // printBoard(board)
                if (game.gameStep < game.countGame) {
                    println("Score")
                    println("${game.user1.name}: ${game.user1.score} ${game.user2.name}: ${game.user2.score}")
                }
                game.gameStep += 1
                game.stepUser += 1

                break
            }

            game.stepUser += 1

        }



        if ( game.gameStep <= game.countGame) {
            println("Game #${game.gameStep}")
            printBoard(board)
        }
    }

    if (game.user1.score == game.user2.score) println("It is a draw")
    else{
        if (game.user1.score > game.user2.score) println("Player ${game.user1.name} won")
        else {
            println("Player ${game.user2.name} won")
        }
    }
    println("Score")
    println("${game.user1.name}: ${game.user1.score} ${game.user2.name}: ${game.user2.score}")
    println("Game over!")

//ens

}

fun cleanBord(board: MutableList<MutableList<Char>>) {
    var rows = board.size
    var columns = board[0].size
    for (i in 0..rows - 1){
        for (j in 0..columns - 1){
            board[i][j] = ' '
        }
    }

}

fun checkEmptyBoard(board: MutableList<MutableList<Char>>): Boolean {
    var rezult = 0
    board.forEach { it.forEach {
        if (it == ' ') ++rezult
    } }
    return  rezult == 0
}

fun fountFreePlace(board: MutableList<MutableList<Char>>, col: Int): Int {
    var rezult = board.size
    for (i in board.lastIndex downTo 0) {
        if (board[i][col] == ' ') {
            rezult = i
            break
        }
    }
    return  rezult
}

//функция распечатки доски
fun printBoard(board: MutableList<MutableList<Char>>) {
    var rows = board.size
    var columns = board[0].size
    for (i in 1..columns) { print(" $i") } //распечатка шапки
    println()
   // repeat(rows) {
   //     repeat(columns + 1) { print("║ ") }
   //     println()
   // }
    for (i in 0..rows - 1){
        for (j in 0..columns - 1){
            print("║${board[i][j]}")
            if (j == columns - 1) print("║")
        }
        println()
    }
    println("╚═${"╩═".repeat(columns - 1)}╝")
}

//функция проверки на число
fun isDig(str: String) : Boolean {
    var rezult = true
     str.forEach { rezult = rezult && it.isDigit() }
    return  rezult
}

// функция проверки на диапазон введеных столбцов
fun isRang(str: String, countCol : Int): Boolean {
    return (str.toInt() in 1..countCol)
}

//функция проверки на выигрышное условие //проверка выигрыша по горизонтали
fun checkWinGorizont(board: MutableList<MutableList<Char>>): Boolean {

    var user1Win = "oooo"
    var user2Win = "****"
    var result = false
    var line = ""
    board.forEach {
        // if (it.containsAll(user1Win) || it.containsAll(user2Win)) result = result || true
        line = it.joinToString<Char>(separator = "")
        if (user1Win in line || user2Win in line) result = result || true
    }
    return result
}

// проверка выигрыша по вертикали
fun checkWinDiagonal(board: MutableList<MutableList<Char>>): Boolean {
    var user1Win = "oooo"
    var user2Win = "****"
    var rezult = false
    var steps = board.size + board[0].size - 1 // количество диагоналей
    var line = ""

    var row = board.size
    var colum = board[0].size

    // распечатка диагонали вправо
    for (i in 1..steps) {
        var start_col = max(0,i- row)
        var count1 = min(i, (colum - start_col))
        var count = min(count1, row)
        for (j in 0 until  count) {
            line += board[min(row, i) - j - 1][start_col + j]
        }
        line += '|'

    }
    line += '+'
    // распечатка диагонали влево
    board.reverse()
    for (i in 1..steps) {
        var start_col = max(0,i- row)
        var count1 = min(i, (colum - start_col))
        var count = min(count1, row)
        for (j in 0 until  count) {
            line += board[min(row, i) - j - 1][start_col + j]
        }
        line += '|'

    }
    board.reverse()
    if (user1Win in line || user2Win in line) rezult = rezult || true
    return  rezult

}
fun checkWinVertical(board: MutableList<MutableList<Char>>): Boolean {
    var user1Win = "oooo"
    var user2Win = "****"
    var rezult = false
    var colNew = board[0].lastIndex
    var line = ""
    for (i in 0..colNew) {
        board.forEach {
            line += it[i]
        }
    }
    if (user1Win in line || user2Win in line) rezult = rezult || true
    return  rezult
}