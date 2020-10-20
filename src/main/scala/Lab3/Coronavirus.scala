package object Lab3

import com.cra.figaro.language._
import com.cra.figaro.algorithm.sampling._
import com.cra.figaro.algorithm.factored._
import com.cra.figaro.library.compound.CPD

object Coronavirus {
  private val febra = Flip(0.06)
  private val tuse = Flip(0.4)

  private val coronavirus = CPD (
    febra, tuse,
    (false, false) -> Flip(0.001),
    (true, false) -> Flip(0.1),
    (false, true) -> Flip(0.9),
    (true, true) -> Flip(0.99)
  )

  private val isCoronavirus = CPD (
    coronavirus,
    false -> Flip(0.01),
    true -> Flip(0.7)
  )

  def main(args: Array[String]): Unit = {
    isCoronavirus.observe(true)

    val alg = VariableElimination(febra, tuse)
    alg.start()
    println("Probabilitatea sa ai covid atunci cand ai febra " + alg.probability(febra, true))
    println("Probabilitatea sa ai covid atunci cand ai tuse " + alg.probability(tuse, true))

    println("Probabilitatea sa ai covid atunci cand nu ai febra " + alg.probability(febra, false))
    println("Probabilitatea sa ai covid atunci cand nu ai tuse " + alg.probability(febra, false))
    alg.kill()
  }
}