package Lab4{

  import com.cra.figaro.algorithm.factored.VariableElimination
  import com.cra.figaro.language.Flip

  object Ex3 {
    /* a
      val x = Flip(0.4)
      val y = Flip(0.4)
      val z = x
      // variabilei z i-a fost asignata valoarea vraiabilei x
      // w va avea valoarea true cu probabilitate 1, deoarece x == x tot timpul
      val w = x ===  z
      println(VariableElimination.probability(w, true))

     */
    /*
      val x = Flip(0.4)
      val y = Flip(0.4)
      // variabilei z i-a fost asignata valoarea vraiabilei y
      val z = y
      // probabilitatea oricarei combinatii de valori de adevar e de 0.25
      // ca cele 2 valori sa aiba aceeasi valoare de adevar este de 0.5
      val w = x === z
      println(VariableElimination.probability(w, true))
    */
  }
}
