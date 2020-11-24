
package TestPractic1 {

  import com.cra.figaro.algorithm.factored.VariableElimination
  import com.cra.figaro.language.{Chain, Flip, Select}
  import com.cra.figaro.library.compound.{If, OneOf}
  // import com.cra.figaro.language.Chain
  import com.cra.figaro.library.compound.RichCPD

  object TestPractic1 {
    def main(args: Array[String]) {
      // Cu o probabilitate de 90% alarma o sa fie setata, iar cu probaiblitate de 10% alarma nu va fi setata
      val alarmaSetata = Flip(0.9)
      // daca alarma este setata, cu o probabilitate de 0.1 nu va suna(va fi oprita si ne vom trezi tarziu), altefl daca nu e setata, vom intarzia cu o probabilitate de 90%
      val trezitTarziu = If(alarmaSetata, Flip(0.1), Flip(0.9))
      // autobuzul poate intarzia cu o probabilitate de 20%
      val intarzieAutobuzul = Flip(0.2)
      // ajung la timp la serviciu cu o probabilitate mult mai mica daca si adorm si intarzie si autobuzul, dar ajung la serviciu cu o probabilitate aproape 1 daca ma trezesc la timp si nu intarzie nici autobuzul
      val ajungLaTimpLaServiciu = RichCPD(trezitTarziu, intarzieAutobuzul,
        (OneOf(true), OneOf(true)) -> Flip(0.1),
        (OneOf(true), OneOf(false)) -> Flip(0.3),
        (OneOf(false), OneOf(true)) -> Flip(0.2),
        (OneOf(false), OneOf(false)) -> Flip(0.9)
      )
      // atunci cand observ ca deja m-am trezit foarte tarziu, probabilitatea sa ajung la serviciu scade drastic
      trezitTarziu.observe(true)
      // Probabilitatea sa intarzii la serviciu in caz ca am adormit
      // va fi o probabilitate foarte mica sa ajung la serviciu
      println(VariableElimination.probability(ajungLaTimpLaServiciu, true)) // 0.26
      trezitTarziu.unobserve()

      // daca ajung lla timp la serviciu, asta inseamna ca nu a intarziat nici autobuzul si nu m-am trezit nici tarziu
      ajungLaTimpLaServiciu.observe(true)
      println(VariableElimination.probability(alarmaSetata, true)) // aproape de 1 (0.953731..
      ajungLaTimpLaServiciu.unobserve()


      println(VariableElimination.probability(trezitTarziu, true)) // 0.18
    }
  }
}

