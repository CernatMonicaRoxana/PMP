package RestMariri

import com.cra.figaro.language.{Constant, Element}

object ex3{
  abstract class State {
    // nr de stari pentru care monitorizam
    val stari = 4

    val tranzitii: Array[Element[Symbol]] = Array.fill(stari)(Constant('a))
  }
}