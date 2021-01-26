package Lab7

import com.cra.figaro.algorithm.factored.VariableElimination
import com.cra.figaro.language.{Chain, Flip}
import com.cra.figaro.library.compound.^^


object Ex1 {
	/* OOP explanation and small example
	abstract class Printer {
		val powerButtonOn = Flip(0.95)
		val paperFlow =
			Select(0.6 -> 'smooth, 0.2 -> 'uneven, 0.2 -> 'jammed)
		val paperJamIndicatorOn =
			If(powerButtonOn,
				CPD(paperFlow,
					'smooth -> Flip(0.1),
					'uneven -> Flip(0.3),
					'jammed -> Flip(0.99)),
				Constant(false))
		val state: Element[Symbol]
	}
	class LaserPrinter extends Printer {
		val tonerLevel = Select(0.7 -> 'high, 0.2 -> 'low, 0.1 -> 'out)
		val tonerLowIndicatorOn =
			If(powerButtonOn,
				CPD(tonerLevel,
					'high -> Flip(0.2),
					'low -> Flip(0.6),
					'out -> Flip(0.99)),
				Constant(false))
		val state =
			Apply(powerButtonOn, tonerLevel, paperFlow,
				(power: Boolean, toner: Symbol, paper: Symbol) => {
					if (power) { //
						if (toner == 'high && paper == 'smooth) 'good
						else if (toner == 'out || paper == 'out) 'out
						else 'poor
					} else 'out
				}
			)
	}
	class InkjetPrinter extends Printer {
		val inkCartridgeEmpty = Flip(0.1)
		val inkCartridgeEmptyIndicator = If(inkCartridgeEmpty, Flip(0.99), Flip(0.3))
		val cloggedNozzle = Flip(0.001)
		val state =
			Apply(powerButtonOn, inkCartridgeEmpty,
				cloggedNozzle, paperFlow,
				(power: Boolean, ink: Boolean,
				 nozzle: Boolean, paper: Symbol) => {
					if (power && !ink && !nozzle) {
						if (paper == 'smooth) 'good
						else if (paper == 'uneven) 'poor
						else 'out
					} else 'out
				}
			)
	}

	class Software {
		val state = Select(0.8 -> 'correct, 0.15 -> 'glitchy, 0.05 -> 'crashed)
	}
	class Network {
		val state = Select(0.7 -> 'up, 0.2 -> 'intermittent, 0.1 -> 'down)
	}
	class User {
		val commandCorrect = Flip(0.65)
	}

	class PrintExperience(printer: Printer, software: Software, network:
	Network, user: User) {
		val numPrintedPages =
			RichCPD(user.commandCorrect, network.state, software.state,
				printer.state,
				(*, *, *, OneOf('out)) -> Constant('zero),
				(*, *, OneOf('crashed), *) -> Constant('zero),
				(*, OneOf('down), *, *) -> Constant('zero),
				(OneOf(false), *, *, *) -> Select(0.3 -> 'zero, 0.6 -> 'some, 0.1
					-> 'all),
				(OneOf(true), *, *, *) -> Select(0.01 -> 'zero, 0.01 -> 'some, 0.98
					-> 'all))
		val printsQuickly =
			Chain(network.state, software.state,
				(network: Symbol, software: Symbol) =>
					if (network == 'down || software == 'crashed) Constant(false)
					else if (network == 'intermittent || software == 'glitchy)
						Flip(0.5)
					else Flip(0.9))
		val goodPrintQuality =
			CPD(printer.state,
				'good -> Flip(0.95),
				'poor -> Flip(0.3),
				'out -> Constant(false))
		val summary =
			Apply(numPrintedPages, printsQuickly, goodPrintQuality,
				(pages: Symbol, quickly: Boolean, quality: Boolean) =>
					if (pages == 'zero) 'none
					else if (pages == 'some || !quickly || !quality) 'poor
					else 'excellent)
	}

	val myLaserPrinter = new LaserPrinter
	val myInkjetPrinter = new InkjetPrinter
	val mySoftware = new Software
	val myNetwork = new Network
	val me = new User
	val myExperience1 = new PrintExperience(myLaserPrinter, mySoftware, myNetwork, me)
	val myExperience2 = new PrintExperience(myInkjetPrinter, mySoftware, myNetwork, me)

	def step1() {
		myExperience1.summary.observe('none)
		val alg = VariableElimination(myLaserPrinter.powerButtonOn, myNetwork.state)
		alg.start()
		println("After observing that printing with the laser printer " + "produces no result:")
		println("Probability laser printer power button is on = " +  alg.probability(myLaserPrinter.powerButtonOn, true))
		println("Probability network is down = " +  alg.probability(myNetwork.state, 'down))
		alg.kill()
	}

	def step2() {
		myExperience2.summary.observe('none)
		val alg = VariableElimination(myLaserPrinter.powerButtonOn, myNetwork.state)
		alg.start()
		println("\nAfter observing that printing with the inkjet printer " + "also produces no result:")
		println("Probability laser printer power button is on = " + alg.probability(myLaserPrinter.powerButtonOn, true))
		println("Probability network is down = " + alg.probability(myNetwork.state, 'down))
		alg.kill()
}
	 */
	/*
	// social network model
	class Topic() extends ElementCollection {
		// creaza un element"hot si il adauga in element collection this specific instance of Topic
		val hot = Flip(0.1)("hot", this)
	}

	val sports = new Topic()
	val politics = new Topic()

	class Person() {
		// interest e un Element[Topic]
		val interest = Uniform("sports", "politics")
	}

	// poster poate fi un argument, postarea depinde de poster, dar nu invers
	// daca posterul ar fi depins de post, nu ar fi mers
	class Post(val poster: Person) extends ElementCollection {
		// If is a sintactic sugar of Chain
		val topic = If(Flip(0.9), poster.interest, Uniform(sports, politics))("topic", this)
	}
	/// a instance of topic is also an ElementCollection


	// conexiunile sunt derivate din Persoane, nu se va putea apela connection(person1, person2) pentru a obtine conexiunea fara a o suprascrie, asa ca o memoram
	class Connection(person1: Person, person2: Person) {
		val connectionType = Uniform("acquaintance", "close friend", "family")
	}

	def generateConnection(pair: (Person, Person)) = new Connection(pair._1, pair._2)
	val connection = memo(generateConnection _)


	class Comment(val post: Post, val commenter: Person) {
		val isHot = post.get[Boolean]("topic.hot")
		val appropriateComment = Apply(post.topic, commenter.interest, isHot, (t1: Topic, t2: Topic, b: Boolean) => (t1 == t2) || b)
		// true daca valorile sunt egale
		val topicMatch = post.topic === commenter.interest
		val pair = ^^(topicMatch, connection(post.poster, commenter).connectionType)
		def constraint(pair: (Boolean, String)) = {
			val (topicMatch, connectionType) = pair // pereche intre topic si conexiune
			if (topicMatch) 1.0 // daca este mach pe topic
			else if (connectionType == "family") 0.8
			else if (connectionType == "close friend") 0.5
			else 0.1
		}
		pair.addConstraint(constraint _)
	}

	def main(args: Array[String]): Unit = {
		val amy = new Person
		val brian = new Person
		val cheryl = new Person

		val post1 = new Post(amy)
		val post2 = new Post(brian)
		val post3 = new Post(cheryl)

		post1.topic.observe("politics")
		post2.topic.observe("sports")
		post3.topic.observe("politics")

		// Amy e interesatade politica, deoarce a postat de 2 ori
		println("Probability Amy's interest is politics = " + VariableElimination.probability(amy.interest, "politics"))
		println("Probability Brian's interest is politics = " + VariableElimination.probability(brian.interest, "politics"))
		println("Probability Cheryl's interest is politics = " + VariableElimination.probability(cheryl.interest, "politics"))
		println("Probability Brian is Amy's family = " + VariableElimination.probability(connection(amy, brian).connectionType, "family"))
		println("Probability Cheryl is Amy's family = " + VariableElimination.probability(connection(amy, cheryl).connectionType, "family"))
		println("Probability Cheryl is Brian's family = " + VariableElimination.probability(connection(brian, cheryl).connectionType, "family"))
	}
	 */

	class ResearchAndDevelopment {
		val state = Flip(0.5)
	}

	class HumanResources {
		val state = Flip(0.5)
	}

	class Production(val rd: ResearchAndDevelopment, val hr: HumanResources) {
		val state = Chain(rd.state, hr.state,
			(rd: Boolean, hr: Boolean) => {
				if (rd && hr) Flip(0.9)
				else if (rd && !hr) Flip(0.5)
				else if (!rd && hr) Flip(0.5)
				else Flip(0.1)
			})
	}

	class Sales(val p: Production) {
		val state = Chain(p.state,
			(p: Boolean) => {
				if (p) Flip(0.9)
				else Flip(0.1)
			})
	}

	class Finance(val hr: HumanResources, val s: Sales) {
		val state = Chain(hr.state, s.state,
			(hr: Boolean, s: Boolean) => {
				if (hr && s) Flip(0.9)
				else if (hr && !s) Flip(0.5)
				else if (!hr && s) Flip(0.5)
				else Flip(0.1)
			})
	}

	class Firm(val rd: ResearchAndDevelopment, val hr: HumanResources, val p: Production, val s: Sales, val f: Finance) {
		val departments = ^^(rd.state, hr.state, p.state, s.state, f.state)
		val health = Chain(departments,
			(d: (Boolean, Boolean, Boolean, Boolean, Boolean)) => {
				val (rd, hr, p, s, f) = d
				if (rd && hr && p && s && f) Flip(0.9)
				else if (p && f && s) Flip(0.8)
				else if (hr && f) Flip(0.5)
				else Flip(0.2)
			})
	}

	def main(args: Array[String]) {
		val rd = new ResearchAndDevelopment()
		val hr = new HumanResources()
		val p = new Production(rd, hr)
		val s = new Sales(p)
		val f = new Finance(hr, s)
		val firm = new Firm(rd, hr, p, s, f)

		hr.state.observe(true)
		rd.state.observe(true)
		println(VariableElimination.probability(firm.health, true))
	}
}