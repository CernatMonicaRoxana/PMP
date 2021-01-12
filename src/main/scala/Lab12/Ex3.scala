package Lab12

import com.cra.figaro.algorithm.factored.VariableElimination
import com.cra.figaro.language._
import com.cra.figaro.algorithm.sampling._
import com.cra.figaro.experimental.normalproposals.Normal
import com.cra.figaro.library.compound.If
import scalax.chart.api._

object Ex3 {
  val x0 = Apply(Normal(0.99, 0.09), (d: Double) => d.max(0).min(1))
  val y0 = Apply(Normal(0.90, 0.10), (d: Double) => d.max(0).min(1))

  val x = Flip(x0)
  val y = Flip(y0)

  val z = If(x === y, Flip(0.9999), Flip(0.0001))
  z.observe(false)
  val veAnswer = VariableElimination.probability(y, true)
  println("The posterior probability that y is true given z false is: " + veAnswer)

  // se geereaza valori false pentru z atunci can x este egal cu y, cea ce e foarte probabil, desi falsitatea lui z nu este foarte probabila
  for (i <- 10000 to 100000 by 10000) {
    var totalSquaredError = 0.0
    for (j <- 1 to 100) {
      val imp = Importance(i, y)
      imp.start()
      val impAnswer = imp.probability(y, true)
      val diff = veAnswer - impAnswer
      totalSquaredError += diff * diff
    }
    val rmse = math.sqrt(totalSquaredError / 100)
    println(i + " samples: RMSE = " + rmse)

    def main(args: Array[String]) {
      val test = Constant("Test")

      val algorithm = Importance(1000, test)
      algorithm.start()

      println(algorithm.probability(test, "Test"))
    }
}