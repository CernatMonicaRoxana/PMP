package Lab8

import com.cra.figaro.language._
import com.cra.figaro.algorithm.sampling._
import com.cra.figaro.library.atomic.discrete._
import com.cra.figaro.library.compound.^^

object Ex1 {
	abstract class Department {
		val health = Uniform(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
		val budget = Uniform(100, 200, 300, 400, 500, 600, 700, 800, 900, 1000)
		val nrOfEmployees = Uniform(11, 12, 13, 14, 15, 16, 17, 18, 19, 20)
	}

	class RnD extends Department {
		val budgetNeeded = Uniform(300, 400, 500)
	}

	class Sales extends Department {
		val profit = Uniform(-200, -100, 0, 200, 300)
	}

	class Production extends Department {
		val unitsProduced = Uniform(1000, 1500, 2000, 2500)
	}

	class HumanResources extends Department {
		val newEmployees = Uniform(1, 2, 3, 4, 5)
	}

	class Finance extends Department {
		val averageProfitOrLoss = Uniform(-100, 0, 100, 200)
	}

	val rnd = new RnD()
	val sales = new Sales()
	val production = new Production()
	val hr = new HumanResources()
	val finance = new Finance()

	val pairHrAndRnd = ^^(hr.nrOfEmployees, rnd.health)
	val pairHrAndSales = ^^(hr.nrOfEmployees, sales.health) // HR influenteaza fiecare departament pentru ca
	val pairHrAndProduction = ^^(hr.nrOfEmployees, production.health) // fiecare departamanet are nevoie de oameni
	val pairHrAndFinance = ^^(hr.nrOfEmployees, finance.health)

	val pairSalesAndFinance = ^^(sales.profit, finance.averageProfitOrLoss) // vanzarile influenteaza finantele

	val pairFinanceAndRnd = ^^(finance.budget, rnd.health)
	val pairFinanceAndSales = ^^(finance.budget, sales.health) // finantele influenteaza bugetele fiecarui departament
	val pairFinanceAndProduction = ^^(finance.budget, production.health) // si implicit "sanatatea", health-ul
	val pairFinanceAndHr = ^^(finance.budget, hr.health)

	val pairRndAndProduction = ^^(rnd.budgetNeeded, production.unitsProduced) // bugetul RnD influenteaza cate produse se fabrrica


	def main(args: Array[String]) {
		val test = Constant("Test")

		val algorithm = Importance(1000, test)
		algorithm.start()

		println(algorithm.probability(test, "Test"))
	}
}