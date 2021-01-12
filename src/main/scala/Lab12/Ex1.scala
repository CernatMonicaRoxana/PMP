package Lab12

import com.cra.figaro.algorithm.factored.VariableElimination
import com.cra.figaro.algorithm.sampling._
import com.cra.figaro.experimental.normalproposals.Normal
import com.cra.figaro.language._
import com.cra.figaro.library.compound.If


object Ex1 extends App with scalax.chart .module.Charting {
	val x0 = Apply(Normal(0.75, 0.3), (d: Double) => d.max(0).min(1))
	val y0 = Apply(Normal(0.4, 0.2), (d: Double) => d.max(0).min(1))

	val x = Flip(x0)
	val y = Flip(y0)

	val z = If(x === y, Flip(0.8), Flip(0.2))
	z.observe(false)
	val veAnswer = VariableElimination.probability(y, true)
	println("The posterior probability that y is true given z false is: " + veAnswer)

	val data = for (i <- 1000 to 10000 by 1000) {
		var totalSquaredError = 0.0
		for (j <- 1 to 100) {
			val imp = Importance(i, y)
			imp.start()
			val impAnswer = imp.probability(y, true)
			val diff = veAnswer - impAnswer
			totalSquaredError += diff * diff
		}
		val rmse = math.sqrt(totalSquaredError / 100)
		yield(i, rmse)
		println(i + " samples: RMSE = " + rmse)
	}

	val chart = XYLineChart(data, title="Errors")
	chart.saveAsPNG("C:\\Users\\cerna\\IdeaProjects\\PMP\\src\\main\\scala\\Lab12\\chartimp.png")


	def main(args: Array[String]) {
		val test = Constant("Test")

		val algorithm = Importance(1000, test)
		algorithm.start()

		println(algorithm.probability(test, "Test"))
}
}