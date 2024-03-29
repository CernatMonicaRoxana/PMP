package Lab11

import com.cra.figaro.language._
import com.cra.figaro.library.compound._
import com.cra.figaro.algorithm.factored._
import com.cra.figaro.algorithm.factored.beliefpropagation.BeliefPropagation
import com.cra.figaro.algorithm.sampling.Importance


object presed {

	def main(args: Array[String])
	{
		val isPresident = Flip(1.0/40000000.0)

		val isLeftHandedPresident = Flip(1.0/2.0)
		val isLeftHandedGeneral = Flip(1.0/10.0)

		val isLeftHanded = If(isPresident, isLeftHandedPresident, isLeftHandedGeneral)

		isLeftHanded.observe(true)

		println('a')
		println(VariableElimination.probability(isPresident, true))
		println(BeliefPropagation.probability(isPresident, true))
		println(Importance.probability(isPresident, true))
		println()

		isLeftHanded.unobserve()

		val wentToHarvardPresident = Flip(3.0/20.0)
		val wentToHarvardGeneral = Flip(1.0/2000.0)

		val wentToHarvard = If(isPresident, wentToHarvardPresident, wentToHarvardGeneral)

		wentToHarvard.observe(true)

		println('b')
		println(VariableElimination.probability(isPresident, true))
		println(BeliefPropagation.probability(isPresident, true))
		println(Importance.probability(isPresident, true))
		println()

		println('c')
		isLeftHanded.observe(true)
		println(VariableElimination.probability(isPresident, true))
		println(BeliefPropagation.probability(isPresident, true))
		println(Importance.probability(isPresident, true))

	}
}