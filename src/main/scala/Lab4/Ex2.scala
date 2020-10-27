import com.cra.figaro.algorithm.factored.VariableElimination
import com.cra.figaro.language.{Flip, Select}
import com.cra.figaro.library.compound.If

package object Lab4

object Ex2 {
  val sunnyToday = Flip(0.2)

  val greetingToday = If (
    sunnyToday,
    Select(0.6 -> "Hello, world!", 0.4 -> "Howdy, universe!"),
    Select(0.2 -> "Hello, world!", 0.8 -> "Oh no, not again")
  )

  val sunnyTomorrow = If(sunnyToday, Flip(0.8), Flip(0.05))

  val greetingTomorrow = If (
    sunnyTomorrow,
    Select(0.6 -> "Hello, world!", 0.4 -> "Howdy, universe!"),
    Select(0.2 -> "Hello, world!", 0.8 -> "Oh no, not again")
  )

  def predict() {
    val result = VariableElimination.probability(greetingToday, "Hello, world!")
    println("Today’s greeting is \"Hello, world!\" " + "with probability " + result + ".")
}
