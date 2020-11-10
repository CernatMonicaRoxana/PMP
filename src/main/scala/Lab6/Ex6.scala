package Lab6

import com.cra.figaro.language._
import com.cra.figaro.algorithm.sampling._
import com.cra.figaro.library.compound.If

object Ex6 {
	/* Functia primeste ca si parametru probabilitatea ca primul jucator sa castige din prima serva, probabilitatea sa castige si probabilitatea sa greseasca, analog pentru jucatorul 2*/
	def tennis (probP1ServeWin: Double, probP1Winner: Double, probP1Error: Double,
							probP2ServeWin: Double, probP2Winner: Double, probP2Error: Double): Element[Boolean] = {
		/* rally primeste ca si parametru daca este prima lovitura sau nu si a carui jucator este randul*/
		def rally(firstShot: Boolean, player1: Boolean): Element[Boolean] = {
				/* jucatorul 1 poate sa castige prima serva daca este prima lovitura si este randul lui
						jucatorul 2 poate sa castige prima serva daca este prima lovitura si este randul lui
						jucatorul 1 poate sa castige daca nu e prima serva si daca este randul lui
						alftel castiga jucatorul 2
				 */
				val pWinner =
					if (firstShot && player1) probP1ServeWin
					else if (firstShot && !player1) probP2ServeWin
					else if (player1) probP1Winner
					else probP2Winner
				/*
				probabilitatea de a gresi este egala cu probabilitatea sa greseasca jucatorul 1, atunci cand e randul sau, sau probabilitatea sa greseasca jucatorul 2 atunci cand nu e randul jucatorului 1
				 */
				val pError = if (player1) probP1Error else probP2Error
			/*
			castigatorul are probabilitatea lui pWinner
			 */
				val winner = Flip(pWinner)
			/*
			erroarea este probabilitatea ca un castigator sa greseasca
			 */
				val error = Flip(pError)
			/*
			daca unul din jucztori castiga, atunci se returneaza jucatorul care a castigat,
			daca un jucator a gresi se retuneaza celalalt jucator, altfel jocul continuapentru loviturile celelalte
			 */
				If(winner, Constant(player1),
					If(error, Constant(!player1),
						rally(firstShot = false, player1 = !player1)))
		}

		/*
		functia primeste ca si parametru jucatorul care serveste
		 */
		def game (p1Serves: Boolean, p1Points: Element[Int],
							p2Points: Element[Int]): Element[Boolean] = {
			/*
			jucatorul 1 castiga un punct daca in prima serva castiga
			 */
			val p1WinsPoint = rally(firstShot = true, player1 = p1Serves)
			/*
			jucatorul primeste 1 un punct daca a castigat serva, altfel ramane cu acelasi punctaj
			 */
			val newP1Points =
				Apply(p1WinsPoint, p1Points, (wins: Boolean, points: Int) =>
					if (wins) points + 1 else points)
					/*
					analog
					 */
			val newP2Points =
				Apply(p1WinsPoint, p2Points, (wins: Boolean, points: Int) =>
					if (wins) points else points + 1)
					/*
					un jucator castiga jocul daca are cel puntin 4 puncte si diferenta punctajelor celor 2 jucatori este cel putin 2
					 */
			val p1WinsGame =
				Apply(newP1Points, newP2Points, (p1: Int, p2: Int) =>
					p1 >= 4 && p1 - p2 >= 2)
			val p2WinsGame =
				Apply(newP2Points, newP1Points, (p2: Int, p1: Int) =>
					p2 >= 4 && p2 - p1 >= 2)
					/*
					un joc se incheie dacaunul din cei 2 jucatori a castigat jocul
					 */
			val gameOver = p1WinsGame || p2WinsGame
			/*
			daca un jucator a castigat jocul, game over
			apoi daca jucatorul nu a castigat jocul se face apel recursic pentru celalalt jucator
			 */
			If(gameOver, p1WinsGame, game(p1Serves, newP1Points, newP2Points))
		}

		def play(p1Serves: Boolean, p1Sets: Element[Int], p2Sets: Element[Int],
							p1Games: Element[Int], p2Games: Element[Int]): Element[Boolean] = {
			val p1WinsGame = game(p1Serves, Constant(0), Constant(0))
			val newP1Games =
				Apply(p1WinsGame, p1Games, p2Games,
					(wins: Boolean, p1: Int, p2: Int) =>
						if (wins) {
							if (p1 >= 5) 0 else p1 + 1
						} else {
							if (p2 >= 5) 0 else p1
						})
			val newP2Games =
				Apply(p1WinsGame, p1Games, p2Games,
					(wins: Boolean, p1: Int, p2: Int) =>
						if (wins) {
							if (p1 >= 5) 0 else p2
						} else {
							if (p2 >= 5) 0 else p2 + 1
						})
			val newP1Sets =
				Apply(p1WinsGame, p1Games, p1Sets,
					(wins: Boolean, games: Int, sets: Int) =>
						if (wins && games == 5) sets + 1 else sets)
			val newP2Sets =
				Apply(p1WinsGame, p2Games, p2Sets,
					(wins: Boolean, games: Int, sets: Int) =>
						if (!wins && games == 5) sets + 1 else sets)
			val matchOver =
				Apply(newP1Sets, newP2Sets, (p1: Int, p2: Int) =>
					p1 >= 2 || p2 >= 2)
			If(matchOver,
				Apply(newP1Sets, (sets: Int) => sets >= 2),
				play(!p1Serves, newP1Sets, newP2Sets, newP1Games, newP2Games))
		}

		play(p1Serves = true, Constant(0), Constant(0), Constant(0), Constant(0))
	}
	def main(args: Array[String]) {
		val test = Constant("Test")

		val algorithm = Importance(1000, test)
		algorithm.start()
		
		println(algorithm.probability(test, "Test"))
	}
}