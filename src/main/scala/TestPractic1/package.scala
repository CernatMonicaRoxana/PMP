import com.cra.figaro.algorithm.factored.VariableElimination
import com.cra.figaro.language.{Flip, Select, }
import com.cra.figaro.language.Chain

import com.cra.figaro.library.compound.If

package TestPractic1 {

  import com.cra.figaro.language.Chain

  object TestPractic1 {
    // Cu o probabilitate de 90% alarma o sa fie setata, iar cu probaiblitate de 10% alarma nu va fi setata
    val alarmaSetata = Flip(0.9)
    // daca alarma este setata, cu o probabilitate de 0.1 nu va suna(va fi oprita si ne vom trezi tarziu), altefl daca nu e setata, vom intarzia cu o probabilitate de 90%
    val nuSunaCeasul = If(alarmaSetata, Flip(0.1), Flip(0.9))
    // autobuzul poate intarzia cu o probabilitate de 20%
    val intarzieAutobuzul = Flip(0.2)
  }
}
