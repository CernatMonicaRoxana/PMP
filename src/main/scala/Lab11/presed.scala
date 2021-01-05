package Lab11

import com.cra.figaro.language._
import com.cra.figaro.library.compound._
import com.cra.figaro.algorithm.factored._
import com.cra.figaro.algorithm.sampling.Importance


object presed {

	val pres = 1 / 40000000
//	print(pres)
	val presedinti = Flip(pres)

	/*
	50% din presedinti stangaci,
	10% pop generala
	 */
	val stangaci = CPD(presedinti,
		(true) -> Flip(0.5),
		(false) -> Flip(0.1)
	)

	/*
	15% din pres au fost la Harvard
	1 / 2000 din pop generala
	 */
	val Harvard = CPD(presedinti,
		(true) -> Flip(0.15),
		(false) -> Flip(0.0005)
	)

	val both = CPD(stangaci, Harvard,
		(true, true) -> Flip(0.075),
		(true, false) -> Flip(0.00025),
		(false, true) -> Flip(0.015),
		(false, false) -> Flip(0.00005)
	)

	def main(args: Array[String]) {

		stangaci.observe(true)

		val alg1 = VariableElimination(presedinti, stangaci)
		alg1.start()
		println("Probabilitatea sa ajunga presedinte daca e stangaci : " + alg1.probability(presedinti, true))
		alg1.kill()
		stangaci.unobserve()

		Harvard.observe(true)

		val alg2 = VariableElimination(presedinti, Harvard)
		alg2.start()
		println("Probabilitatea sa ajunga presedinte daca a invatat la Harvard : " + alg2.probability(presedinti, true))
		alg2.kill()
		Harvard.unobserve()

		Harvard.observe(true)
		stangaci.observe(true)
		both.observe(true)

		val alg3 = VariableElimination(presedinti, both)
		alg3.start()
		println("Probabilitatea sa ajunga presedinte daca a invatat la Harvard si e stangaci : " + alg3.probability(presedinti, true))
		alg3.kill()
		Harvard.unobserve()

		val alg4 = Importance(100, stangaci)
		alg4.start()
		println("Probabilitatea ca un presedinte nu sa fie stangaci: "+ alg4.probability(stangaci, false))
	}
}