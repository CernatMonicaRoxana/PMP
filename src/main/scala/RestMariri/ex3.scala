package RestMariri

import com.cra.figaro.language.{Constant, Element, Select}
import com.cra.figaro.library.compound.CPD

object ex3{
  abstract class State {
    // nr de stari pentru care monitorizam
    val stari = 4

    val tranzitii: Array[Element[Symbol]] = Array.fill(stari)(Constant('a))
    // in prima instnta pot sa ma aflu in oricare din cele 4 stari cu probbilitati egale
    tranzitii(0) = Select(0.25 -> 'A, 0.25 -> 'D, 0.25 -> 'C, 0.25 -> 'D)
    for {i <- 1 until stari} {
      // tin cont de tranzitia anterioara
      tranzitii(i) = CPD(tranzitii(i-1),
      // raman cu prob 0.721 in starea A, ma duc in B cu prob de 0,202
        'A -> Select(0.721 -> 'A, 0.202 -> 'B, 0.067 -> 'C, 0.1 ->'D),
        'B -> Select(0 -> 'A, 0.581 -> 'B, 0.407 -> 'C, 0.012 ->'D),
        'C -> Select(0 -> 'A, 0 -> 'B, 0.75 -> 'C, 0.25 ->'D),
        'D -> Select(0 -> 'A, 0 -> 'B, 0 -> 'C, 1.0 ->'D))
    }

  }
}