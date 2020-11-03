import com.cra.figaro.algorithm.factored.VariableElimination
import com.cra.figaro.language.Apply
import com.cra.figaro.library.atomic.discrete.FromRange

package Lab4 {
  object Ex4 {
    def main(args: Array[String]): Unit = {
      val die1 = FromRange(1, 7)
      val die2 = FromRange(1, 7)
      val die3 = FromRange(1, 7)

      val total = Apply(die1, die2, die3,  (i1: Int, i2: Int, i3: Int ) => i1 + i2 + i3)
      println(VariableElimination.probability(total, 11))

      // aceasta solutie merge pentru maxim 11 zaruri
    }
  }
}
