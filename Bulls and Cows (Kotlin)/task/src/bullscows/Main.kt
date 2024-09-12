package bullscows

fun main() {
    val game = BullsAndCowsGame()
    game.start()

}

class BullsAndCowsGame() {
    private val gameTitle: String = "Bulls and cows."
    private val numberOfDigits: Int = 4
    private lateinit var code: IntArray
    var turn: Int = 1


    init {

    }

    fun start() {

        println("The secret code is prepared: ${"*".repeat(numberOfDigits)}\n")


        while (true) {
            println("Turn $turn. Answer:")

            val answer = readln()
            val grade = answer.grade()

            println("Grade: $grade\n")
            turn += 1

            if (grade == "4 bulls") break
        }

        println("Congrats! The secret code is $code.")

    }

    fun String.grade(): String {
        return "1 cow"
    }


}



data class Player(val name: String)

