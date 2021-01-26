package  Partial2

import com.cra.figaro.language.{Apply, Element, Select, Universe}
import com.cra.figaro.library.compound.If


object test {


  abstract class Vreme {
    // orele
    val ore : Element[Int]
    // initializam array-ul cu vremea care este la momentul 0
    //se selecteaza cu o probabilitate de 0,5 posibilitatea sa fie innrat
    val vreme: Element[Symbol] = Select(0.5 -> 'insoit, 0.3 ->'innorat, 0.2 -> 'ploios )
  }

  class initialState{
    // incepem de la momentul 0
    val ore = 0
  }

  class NextState(curent: Vreme) extends Vreme {
    val vreme = If { curent.ore
      // daca la ora actuala este insorit, cu probabilitate de 0.6, in urmatoarea ora va fi tot insorit
      'insorit -> Select(0.6 -> 'insorit, 0.1 -> 'ploios, 0.3 -> 'innorat)
      'ploios -> Select(0.45 -> 'ploios, 0.15 -> 'insorit, 0.4 -> 'innorat)
      'innorat -> Select(0.5 -> 'innorat, 0.15 -> 'insorit, 0.35 -> 'ploios)
    }


  }

  //
  def nextUniverse(previous: Universe): Universe = {

    val next = Universe.createNew()
    val oraAnterioara = previous.get[]

    val newState = new NextState(oraAnterioara)



}
