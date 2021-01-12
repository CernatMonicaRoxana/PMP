package Lab12

import com.cra.figaro.algorithm.factored.VariableElimination
import com.cra.figaro.language._
import com.cra.figaro.algorithm.sampling._
import com.cra.figaro.experimental.normalproposals.Normal
import com.cra.figaro.library.compound.If
import scalax.chart.api._

object Ex2{
  val x0 = Apply(Normal(0.75, 0.3), (d: Double) => d.max(0).min(1))
  val y0 = Apply(Normal(0.4, 0.2), (d: Double) => d.max(0).min(1))

  val x = Flip(x0)
  val y = Flip(y0)

  val z = If(x === y, Flip(0.8), Flip(0.2))
  z.observe(false)
  val veAnswer = VariableElimination.probability(y, true)
  println("The posterior probability that y is true given z false is: " + veAnswer)

  val data = for {i <- 10000 to 100000 by 10000} {
    var totalSquaredError = 0.0
    for {j <- 1 to 100} {
      val mh = MetropolisHastings(i, ProposalScheme.default, y)
      mh.start()
      val mhAnswer = mh.probability(y, true)
      val diff = veAnswer - mhpAnswer
      totalSquaredError += diff * diff
    }
    val rmse = math.sqrt(totalSquaredError / 100)
    yield(i, rmse)
    println(i + " samples: RMSE = " + rmse)
  }

  val chart = XYLineChart(data, title="Errors")
  chart.saveAsPNG("C:\\Users\\cerna\\IdeaProjects\\PMP\\src\\main\\scala\\Lab12\\chartmh.png")
}

