## String calculator by JustDeax
This is a **calculator**. He parses **string expression** and gives the value of Double type.

**Function**:
- sqrt(x), cbrt(x), root(x, y), pow10(x)
- log(x, y), log10(x), loge(x, y) or in(x, y)
- sin(x), cos(x), tan(x), tg(x), ctg(x) - Radiant
- sind(x), cosd(x), tagd(x), tgd(x), ctgd(x) - Degrees
- arcsin(x), arccos(x), arctan(x), arctg(x), arcctg(x) - Radiant
- arcsind(x), arccosd(x), arctand(x), arctgd(x), arcctg(x) - Degrees

**Operation**:
- -, +, *, /, ^, !

**Features**:
- Automatic definition of negative numbers
- automatic arrangement of multiplication before brackets
- VariablesParser.java, make variables for expressions
- Kotlin calculator file

## How to use
1. First: Just copy and paste Calculator.kt file to your project
2. Second: Use one of this methods
#### Simple method

```kotlin
val expression = readln() //"2 + 2"

val answer = ExpressionParser.fullCalculate(expression)

print(answer) //4
```
#### Advanced method
```kotlin
val expression = readln() //"2 + 5"

val postfix = ExpressionParser.parse(expression) // 2 5 +

val answer = ExpressionParser.calculate(postfix)

print(answer) //7
```
#### Check the Main.kt file for more examples
