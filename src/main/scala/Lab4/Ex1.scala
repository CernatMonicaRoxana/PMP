package Lab4 

import com.cra.figaro.algorithm.factored.VariableElimination
import com.cra.figaro.language._
import com.cra.figaro.algorithm.sampling._
import com.cra.figaro.library.compound.If

object Ex1 {
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

	val rightSideOfTheBed = Flip(0.5)

	val sideOfTheBed = If (
		rightSideOfTheBed,
		Select(0.6 -> "Hello, world!", 0.4 -> "Howdy, universe!"),
		Select(1.0 -> "Oh no, not again")
	)

	def predictWrongSideOfTheBed(): Unit = {
		val res = VariableElimination.probability(sideOfTheBed, "Oh no, not again!")
		println("I got out of the wrong side of the bed with probability " + res)
	}

	def predict() {
		val result = VariableElimination.probability(greetingToday, "Hello, world!")
		println("Today’s greeting is \"Hello, world!\" " +
			"with probability " + result + ".")
	}

	def infer() {
		greetingToday.observe("Hello, world!")
		val result = VariableElimination.probability(sunnyToday, true)
		println("If today's greeting is \"Hello, world!\", today’s " + "weather is sunny with probability " + result + ".")
	}

	def learnAndPredict() {
		greetingToday.observe("Hello, world!")
		val result = VariableElimination.probability(greetingTomorrow, "Hello, world!")
		println("If today's greeting is \"Hello, world!\", " + "tomorrow's greeting will be \"Hello, world!\" " +
			"with probability " + result + ".")
	}


	def main(args: Array[String]) {
		predictWrongSideOfTheBed()
		predict()
		infer()
		learnAndPredict()
	}

}