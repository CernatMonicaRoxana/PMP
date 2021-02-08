package RestMariri

import com.cra.figaro.algorithm.factored.VariableElimination
import com.cra.figaro.algorithm.sampling.Importance
import com.cra.figaro.experimental.normalproposals.Normal
import com.cra.figaro.language.Select

object ex2 {
  // A, a
  val mean = 7
  val variance = 5
  // temperatura umreaza o distributie normala cu media 7 si varianta 5
  val temperature = Normal(mean, variance)

  //b
  // cu probabilitati egale se aleg valorile pentru varianta
  variance = Select(0.5 -> 20, 0.5 -> 30)

  //B
  var importance = Importance(1000, temperature)
  importance.start()

  println("Probabilitarea ca temperatura sa fie 20 " + VariableElimination.probability(temperature, 20))
  println("Probabilitarea ca temperatura sa fie 50 " + VariableElimination.probability(temperature, 50))
}