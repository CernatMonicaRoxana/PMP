package Lab4 {

  import com.cra.figaro.algorithm.factored.VariableElimination
  import com.cra.figaro.language.Apply
  import com.cra.figaro.library.atomic.discrete.FromRange

  object ex5 {
    def main(args: Array[String]): Unit = {
      val die1 = FromRange(1, 7)
      val die2 = FromRange(1, 7)
      val total = Apply(die1, die2, (i1: Int, i2: Int) => i1 + i2)
      total.addCondition((i: Int) => i > 8)
      println(VariableElimination.probability(die1, 6))

      total.removeConditions()

      total.addCondition((i: Int) => i < 11)
      println(VariableElimination.probability(die2, 2))
    }
  }
}
