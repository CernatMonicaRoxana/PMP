package Lab9

	import com.cra.figaro.library.compound.^^
	import com.cra.figaro.algorithm.filtering.ParticleFilter
	import com.cra.figaro.language.{Apply,Flip, _}

	object Ex1 {

		// def initial() {
		// }
		def transition(learned: Int, difficulty: Int): (Element[(Int, Int)]) = {

			val newDifficulty = Apply (Flip (difficulty / 80.0), (b: Boolean) => if (b) - 1 else difficulty + 5)

			^^(learned, newDifficulty)
		}

		def nextUniverse(previous: Universe): Universe = {
			val next = Universe.createNew()
			val previousLearned = previous.get[Int]("learned")
			val previousDifficulty = previous.get[Int]("difficulty")
			val newState = Chain(previousLearned, previousDifficulty, transition)
			Apply(newState, (s: (Int, Int, Int)) -> s._1)("learned", next)
			Apply(newState, (s: (Int, Int, Int)) -> s._2)("score", next)
			Apply(newState, (s: (Int, Int, Int)) -> s._3)("difficulty", next)
			next // return nextUniverse
		}

		def main(args: Array[String]) {
			val arrivingObservation = List(Some(7), Some(8), Some(9), None, None, None, None, None, None, None)
			val alg = ParticleFilter(initial, nextUniverse, 100)
			alg.start()
			for (chapter <- 1 to 10) {
				val evidence = {
					arrivingObservation(chapter) match {
						case None => List()
						case Some(n) => List(NamedEvidence("score", Observation(n)))
					}
				}
				alg.advanceTime(evidence)
				print("Chapter " + chapter + ":")
				println("expected learning = " + alg.currentExpectation("score", (i: Int) => i))
			}
		}
}