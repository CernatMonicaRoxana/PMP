import com.cra.figaro.algorithm.factored.VariableElimination
import com.cra.figaro.language.{Flip, Select}
import com.cra.figaro.library.compound.If

package object Lab4

object Ex2a {
  val sunnyToday = Flip(0.2)

  val greetingToday = If(
    sunnyToday,
    Select(0.6 -> "Hello, world!", 0.4 -> "Howdy, universe!"),
    Select(0.2 -> "Hello, world!", 0.8 -> "Oh no, not again")
  )

  val sunnyTomorrow = If(sunnyToday, Flip(0.8), Flip(0.05))

  val greetingTomorrow = If(
    sunnyTomorrow,
    Select(0.6 -> "Hello, world!", 0.4 -> "Howdy, universe!"),
    Select(0.2 -> "Hello, world!", 0.8 -> "Oh no, not again")
  )

  def predict() {
    val result = VariableElimination.probability(greetingToday, "Hello, world!")
    println("Today’s greeting is \"Hello, world!\" " + "with probability " + result + ".")
  }

  def infer() {
    greetingToday.observe("Hello, world!")
    val result = VariableElimination.probability(sunnyToday, true)
    println("If today's greeting is \"Hello, world!\", today’s " +
      "weather is sunny with probability " + result + ".")
  }

  def learnAndPredict() {
    greetingToday.observe("Hello, world!")
    val result = VariableElimination.probability(greetingTomorrow,
      "Hello, world!")
    println("If today's greeting is \"Hello, world!\", " +
      "tomorrow's greeting will be \"Hello, world!\" " +
      "with probability " + result + ".")
  }

  def observeGreetingToday(): Unit = {
    greetingToday.observe("Oh no, not again")  // invatam gretiing-ul zile de astazi
    val res = VariableElimination.probability(greetingToday, "Oh no, not again") // inferam pentru a afla greetingul zilei de astazi

    println("If today's greeting is \"Oh no, not again\", "+
        "Today's greeting will be \"Oh no, not again\" " +
    "with probability " + res)
  }

  def main(args: Array[String]) {
    observeGreetingToday()
    predict()
    infer()
    learnAndPredict()
  }
}
