package Lab5

import com.cra.figaro.library.atomic.discrete
import com.cra.figaro.language.Chain
import com.cra.figaro.library.compound.{RichCPD, OneOf, *}
import com.cra.figaro.language.{Flip, Apply}
import com.cra.figaro.algorithm.factored.VariableElimination

object Ex2 {
  def main(args: Array[String]) {
    val cards = List(5, 4, 3, 2, 1)

    val player1Card = discrete.Uniform(cards: _*)
    val player2Card = Chain(player1Card,
      (card: Int) =>
        discrete.Uniform(cards.filter(_ != card): _*))
    val player1Bet1 = RichCPD(player1Card,
      OneOf(5, 4, 3) -> Flip(0.9),
      * -> Flip(0.4))
    val player2Bet = RichCPD(player2Card, player1Bet1,
      (OneOf(5, 4), *) -> Flip(0.9),
      (*, OneOf(false)) -> Flip(0.5),
      (*, *) -> Flip(0.1))
    val player1Bet2 = Apply(player1Card, player1Bet1, player2Bet,
      (card: Int, bet11: Boolean, bet2: Boolean) =>
        !bet11 && bet2 && (card == 5 || card == 4))

    val player1Gain = Apply(player1Card, player2Card, player1Bet1, player2Bet, player1Bet2,
      (card1: Int, card2: Int, bet11: Boolean, bet2: Boolean, bet12: Boolean) =>
        if (!bet11 && !bet2) 0.0
        else if (bet11 && !bet2) 1.0
        else if (!bet11 && bet2 && !bet12) -1.0
        else if (card1 > card2) 2.0
        else -2.0)

    val seq1 = Apply(player1Card, player1Bet1, player2Bet, player1Bet2,
      (card1: Int, bet11: Boolean, bet2: Boolean, bet12: Boolean) =>
        card1 == 4 && !bet11 && bet2 && bet12)
    val seq2 = Apply(player2Card, player1Bet1, player2Bet, player1Bet2,
      (card2: Int, bet11: Boolean, bet2: Boolean, bet12: Boolean) =>
        card2 == 3 && bet11 && !bet2)
    val seq3 = Apply(player1Bet1, player2Bet, player1Bet2,
      (bet11: Boolean, bet2: Boolean, bet12: Boolean) =>
        bet11 && bet2)

    player1Card.observe(4)
    player1Bet1.observe(true)
    val alg1 = VariableElimination(player1Gain)
    alg1.start()
    alg1.stop()
    println("Expected gain for betting:" + alg1.mean(player1Gain))
    player1Bet1.observe(false)
    val alg2 = VariableElimination(player1Gain)
    alg2.start()
    alg2.stop()
    println("Expected gain for passing:" + alg2.mean(player1Gain))
    player1Card.unobserve()
    player1Bet1.unobserve()

    player2Card.observe(3)
    player1Bet1.observe(true)
    player2Bet.observe(true)
    val alg3 = VariableElimination(player1Gain)
    alg3.start()
    alg3.stop()
    println("Expected gain for betting:" + alg3.mean(player1Gain))
    player2Bet.observe(false)
    val alg4 = VariableElimination(player1Gain)
    alg4.start()
    alg4.stop()
    println("Expected gain for passing:" + alg4.mean(player1Gain))

    player2Card.unobserve()
    player1Bet1.unobserve()
    player2Bet.unobserve()

    val alg5 = VariableElimination(seq1)
    alg5.start()
    alg5.stop()
    println(alg5.probability(seq1, true))
    val alg6 = VariableElimination(seq2)
    alg6.start()
    alg6.stop()
    println(alg6.probability(seq2, true))
    val alg7 = VariableElimination(seq3)
    alg7.start()
    alg7.stop()
    println(alg7.probability(seq3, true))

  }
}