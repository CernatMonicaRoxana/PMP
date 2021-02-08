package RestMariri

import breeze.stats.distributions.Binomial
import com.cra.figaro.algorithm.factored.VariableElimination
import com.cra.figaro.language.{Apply, Flip}


object ex1 {

  // monitorizam pentru 10 zile
  val zile = 10
//  // array de simboluri in care stocam tipul de zile ninge/nu ninge, il initializam ca si cum nu ar ninge
//  val nrzileNinge : Array[Element[Symbol]] = Array.fill(zile)(Constant('nuNinge))
//
//  for {zi <- 0 until zile} {
//    //probabilitatea sa ninga in prima zi este de 0.6, iar probabilitatea sa nu ninga este de 0.4
//    nrzileNinge(zi) = Select(0.6 -> 'ninge, 0.4 -> 'nuNinge)
//  }
  val ninge = Flip(0.6)
  // calitatea saptamanii atunci cand ninge mai mult de 5 zile
  //o distributie binomiala pe 7 zile
  val nrZileNinge = Binomial(7, ninge)
  // daca sunt mai mult de 5 zile in care ninge, atunci este prea multa ninsoare, daca sunt cel mult 2 zile in care ninge, este prea putina ninsoare, altfel este o cantitate normala
  val calitateSapt = Apply(nrZileNinge,
    (i:Int) =>
      if (i > 5) "prea multa ninsoare";
      else if (i <= 2) "prea putina ninsoare";
      else "normala"
  )

  def main(args: Array[String]): Unit = {
    println("Probabilitatea ca nr de zile in care ninge sa duca la o calitate normala a saptamanii este " + VariableElimination.probability(calitateSapt, "normala") )
  }

}