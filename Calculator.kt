package calculator

/* Author: https://github.com/JustDeax */

import java.util.ArrayDeque
import java.util.Deque
import java.util.StringTokenizer
import kotlin.math.*
import kotlin.text.iterator

object ExpressionParser {
    private const val OPERATORS = "+-*/^!,"
    private const val DELIMITERS = "() $OPERATORS"
    const val ERROR_BRACKETS = "Parentheses are mismatched."
    const val ERROR_EXPRESSION = "Invalid expression"
    const val ERROR_NUMBER = "Incorrect number in the expression"

    private fun isDelimiter(token: String): Boolean {
        if (token.length != 1)
            return false
        for (i in DELIMITERS)
            if (token[0] == i)
                return true
        return false
    }

    private fun isOperator(token: String): Boolean {
        if (token == "u-")
            return true
        for (element in OPERATORS)
            if (token[0] == element)
                return true
        return false
    }

    private fun isFunction(token: String): Boolean {
        return when(token.lowercase()) {
            "sqrt", "cbrt", "root", "pow10",
            "log", "log10", "loge", "in",
            "sin", "cos", "tan", "tg", "ctg",
            "sind", "cosd", "tagd", "tgd", "ctgd",
            "arcsin", "arccos", "arctan", "arctg", "arcctg",
            "arcsind", "arccosd", "arctand", "arctgd", "arcctgd" -> true
            else -> false
        }
    }

    private fun isNumber(token: Char): Boolean {
        return when (token) {
            '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', ')' -> true
            else -> false
        }
    }

    private fun priority(token: String): Int {
        return when (token) {
            "(" -> 1
            "+", "-" -> 2
            "*", "/" -> 3
            "^" -> 4
            else -> 5
        }
    }

    fun parse(str: String): List<String> {
        return try {
            baseParse(str)
        } catch (_: NoSuchElementException) {
            listOf(ERROR_BRACKETS)
        }
    }

    private fun baseParse(str: String): List<String> {
        //Add multiplication before numbers or (
        val chars = str.toCharArray()
        val expression = ArrayList<Char>(20)

        for (i in chars.indices) {
            if (i > 0 && isNumber(chars[i-1]) && chars[i] == '(')
                expression.add('*')
            expression.add(chars[i])
        }

        //Parse to Reverse Polka Notation
        val postfix: MutableList<String> = ArrayList()
        val stack: Deque<String> = ArrayDeque()
        val tokenizer = StringTokenizer(expression.joinToString(""), DELIMITERS, true)
        var previous = ""
        var current: String

        while (tokenizer.hasMoreTokens()) {
            current = tokenizer.nextToken()
            if (current == " " || current == ",")
                continue
            else if (isFunction(current))
                stack.push(current)
            else if (current.lowercase() == "p" || current.lowercase() == "pi")
                postfix.add("${Math.PI}")
            else if (current.lowercase() == "e")
                postfix.add("${Math.E}")
            else if (isDelimiter(current)) {
                if (current == "(") {
                    stack.push(current)
                } else if (current == ")") {
                    while (stack.peek() != "(")
                        postfix.add(stack.pop())
                    stack.pop()
                    if (!stack.isEmpty() && isFunction(stack.peek()))
                        postfix.add(stack.pop())
                } else {
                    if (current == "-" && (previous == "" || isDelimiter(previous) && previous != ")"))
                        current = "u-"
                    else while (!stack.isEmpty() && priority(current) <= priority(stack.peek()))
                        postfix.add(stack.pop())
                    stack.push(current)
                }
            } else
                postfix.add(current)
            previous = current
        }

        while (!stack.isEmpty()) {
            if (isOperator(stack.peek()))
                postfix.add(stack.pop())
            else
                return parse("$str)")
        }

        return postfix
    }

    fun calculate(postfix: List<String>): String {
        return try {
            baseCalculate(postfix).toString()
        } catch (_: NoSuchElementException) {
            ERROR_EXPRESSION
        } catch (_: NumberFormatException) {
            ERROR_NUMBER
        }
    }

    private fun baseCalculate(postfix: List<String>): Double {
        val stack: Deque<Double> = ArrayDeque()

        fun x() = stack.pop()

        for (x in postfix) {
            when (x.lowercase()) {
                "sqrt" -> stack.push(sqrt(x()))
                "cbrt" -> stack.push(cbrt(x()))
                "pow10" -> stack.push(10.0.pow(x()))
                "log10" -> stack.push(log(x(), 10.0))
                "loge", "in" -> stack.push(log(x(), Math.E))
                "log" -> { val b = x(); val a = x(); stack.push(log(a, b)) }
                "root" -> { val b = x(); val a = x(); stack.push(exp(ln(b)/a)) }
                "sin" -> stack.push(sin(x()))
                "sind" -> {
                    when (val a = x()) {
                        0.0, 360.0, 180.0 -> stack.push(0.0)
                        90.0 -> stack.push(1.0)
                        30.0 -> stack.push(0.5)
                        45.0 -> stack.push(sqrt(2.0)/2)
                        60.0 -> stack.push(sqrt(3.0)/2)
                        270.0 -> stack.push(-1.0)
                        else -> stack.push(sin(Math.toRadians(a)))
                    } }
                "cos" -> stack.push(cos(x()))
                "cosd" -> {
                    when (val a = x()) {
                        0.0, 360.0 -> stack.push(1.0)
                        30.0 -> stack.push(sqrt(3.0)/2)
                        45.0 -> stack.push(sqrt(2.0)/2)
                        60.0 -> stack.push(0.5)
                        90.0, 270.0 -> stack.push(0.0)
                        180.0 -> stack.push(-1.0)
                        else -> stack.push(cos(Math.toRadians(a)))
                    } }
                "tg", "tan" -> stack.push(tan(x()))
                "tgd", "tand" -> {
                    when (val a = x()) {
                        0.0 -> stack.push(0.0)
                        30.0 -> stack.push(1/sqrt(3.0))
                        45.0 -> stack.push(1.0)
                        60.0 -> stack.push(sqrt(3.0))
                        90.0, 180.0, 270.0, 360.0 -> stack.push(0.0)
                        else -> stack.push(tan(Math.toRadians(a)))
                    } }
                "ctg" -> stack.push(1/tan(x()))
                "ctgd" -> {
                    when (val a = x()) {
                        0.0, 90.0, 180.0, 270.0, 360.0 -> stack.push(0.0)
                        30.0 -> stack.push(sqrt(3.0))
                        45.0 -> stack.push(1.0)
                        60.0 -> stack.push(1/sqrt(3.0))
                        else -> stack.push(1/tan(Math.toRadians(a)))
                    } }
                "arcsin" -> stack.push(asin(x()))
                "arccos" -> stack.push(acos(x()))
                "arctan", "arctg" -> stack.push(atan(x()))
                "arcctg" -> stack.push(atan(1 / x()))
                "arcsind" -> stack.push(Math.toDegrees(asin(x())))
                "arccosd" -> stack.push(Math.toDegrees(acos(x())))
                "arctand", "arctgd" -> stack.push(Math.toDegrees(atan(x())))
                "arcctgd" -> stack.push(Math.toDegrees(atan(1 / x())))
                "u-" -> stack.push(-x())
                "+" -> stack.push(x() + x())
                "*" -> stack.push(x() * x())
                "-" -> { val b = x(); val a = x(); stack.push(a - b) }
                "/" -> { val b = x(); val a = x(); stack.push(a / b) }
                "^" -> { val b = x(); val a = x(); stack.push(a.pow(b)) }
                "!" -> {
                    var a = 1; val b = x()
                    if (b > 0)
                        for (i in 2..b.toInt())
                            a *= i
                    else
                        for (i in b.toInt()..-1)
                            a *= i
                    stack.push(a.toDouble())
                }
                else -> stack.push(x.toDouble())
            }
        }
        return stack.pop()
    }

    fun fullCalculate(expression: String) = calculate(parse(expression))
}
