package calculator

fun main() {
    print("Method for using: (0-3) ")

    val method = readln().toInt()

    while (method == 0) {
        val expression = readln()

        val postfix = ExpressionParser.parse(expression)
        for (x in postfix)
            print("$x ")

        val answer = ExpressionParser.calculate(postfix)
        println("\n= $answer")
    }

    while (method == 1) {
        val expression = readln()

        val answer = ExpressionParser.fullCalculate(expression)

        print(answer)
    }

    while (method == 2) {
        val expression = readln() //"2 + 5"

        val postfix = ExpressionParser.parse(expression)

        val answer = ExpressionParser.calculate(postfix)

        print(answer)
    }

    while (method == 3) {
        val expression = readln()

        ExpressionParser.apply {
            println(
                calculate(parse(expression))
            )
        }
    }
}