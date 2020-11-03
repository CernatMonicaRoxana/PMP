package Lab5

import com.cra.figaro.library.atomic.discrete
import com.cra.figaro.language.Chain
import com.cra.figaro.library.compound.{RichCPD, OneOf, *}
import com.cra.figaro.language.{Flip, Constant, Apply}
import com.cra.figaro.algorithm.factored.VariableElimination

object Ex1 {
	/* // 1
		5 carti
		2 jucatori
		primul jucator extrage o carte *oricare*
		al doilea extrage o carte conditionat de faptul ca primul jucator a extras si el o carte *4 ramase*
		pentru fiecare carte a primului jucator exista 4 lumi diferite -> 5 * 4 = 20 lumi ->  o lume are probabilitatea 1/20
	 */

	/*
	 // 2
	 stim sigur ca ne aflam intr-o lume in care player1 nu are rege/ regina - > 4 lumi petru fiecare carte = 16
	 prob ca al 2-lea player sa aiba ace stiind ca primul are rege/regina
	 -> rege de spada => 2 carti cu simbolul spada
	 -> rege inima =>  3 carti cu spada
	 analog regina
	 ->  5 + 5/ 16 -> probab ca al 2-lea jucator sa aiba o spada = 10 / 16
	 */

	/*
		// 3
		variabile : cartile, cartile extrase de jucatori, care jucator pariaza sau paseaza
		dependente :
		-> cartea aleasa de jucatorul 2 este conditionata de cartea aleasade jucatorul 1
		-> playerul 1 pariaza sau paseaza in functie de cartea pe care o are
		-> playerul 2 pariaza daca are carte ft mare(ace sau rege de spada) sau daca primul jucator nu a pariat
		->playerul 1 pariaza daca nu a priat in prima tura, daca al doilea jucator a pariat si daaca are o carte ft mare

	 */
	def main(args: Array[String]) {
		// To keep the code simple, I just make the cards an integer
		val cards = List(5, 4, 3, 2, 1)
		// The discrete uniform distribution chooses uniformly from a fixed
		// set of possibilities
		val player1Card = discrete.Uniform(cards: _*)
		val player2Card =
			Chain(player1Card, (card: Int) =>
				// Player 2 can get any card except the first player’s card
				discrete.Uniform(cards.filter(_ != card): _*))
		val player1Bet1 =
			RichCPD(player1Card,
				// Player 1 is more likely to bet with a higher card,
				// but will sometimes bet with a lower card to bluff
				OneOf(5, 4, 3) -> Flip(0.9),
				* -> Flip(0.01) // ×××Change this for part (c)×××
			)
		val player2Bet =
			RichCPD(player2Card, player1Bet1,
				(OneOf(5, 4), *) -> Flip(0.9),
				(*, OneOf(false)) -> Flip(0.5),
				(*, *) -> Flip(0.1))
		val player1Bet2 =
			Apply(player1Card, player1Bet1, player2Bet,
				(card: Int, bet11: Boolean, bet2: Boolean) =>
					// Player 1’s second bet is only relevant if she passed the
					// first time and player 2 bet
					!bet11 && bet2 && (card == 5 || card == 4))
		// This element represents the gain to player 1 from the game. I have
		// made it an Element[Double] so I can query its mean.
		val player1Gain = {
		Apply(player1Card, player2Card, player1Bet1, player2Bet, player1Bet2,
			(card1: Int, card2: Int, bet11: Boolean,
			 bet2: Boolean, bet12: Boolean) =>
				if (!bet11 && !bet2) 0.0
				else if (bet11 && !bet2) 1.0
				else if (!bet11 && bet2 && !bet12) -1.0
				else if (card1 > card2) 2.0
				else -2.0)
		}

		//a
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

		// b
		player2Card.observe(3)
		player1Bet1.observe(true)
		player2Bet.observe(true)
		val alg3 = VariableElimination(player1Gain)
		alg3.start()
		alg3.stop()
		println("Expected gain for betting:" + alg3.mean(player1Gain)) // -1.09 player 2 will lose
		player2Bet.observe(false)
		val alg4 = VariableElimination(player1Gain)
		alg4.start()
		alg4.stop()
		println("Expected gain for passing:" + alg4.mean(player1Gain))
	}
	}