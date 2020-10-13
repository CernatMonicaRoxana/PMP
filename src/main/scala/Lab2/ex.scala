abstract class Persoana{
}

class Student(nume: String, prenume: String, an: Integer, var materii: Array[(String, Integer)]) extends Persoana () {
  def setNota(materie: String, nota: Integer): Unit = {
    var i = 0
    while (materii(i)._1 != materie && i < materii.length) {
      i += 1
    }
    if (i < materii.length) {
      materii(i) = (materie, nota)
    }
  }

  def getNota(materie: String): Integer = {
    val res = materii.find(tuple => tuple._1 == materie)

    if (res.isEmpty) {
      0
    } else {
      res.get._2
    }
  }

  def addMaterie(materie: String, nota: Integer): Unit = {
    materii = materii :+ (materie, nota)
  }
}

class Profesor (nume: String, prenume: String, materie: String) extends Persoana{
}


object Lab2 extends App {
  var student = new Student("ion", "ion", 2, Array(("mate", 2), ("engleza", 3)))
  student.addMaterie("fizica", 3)
  student.setNota("mate", 10)
  println(student.getNota("fizica"))
  println(student.getNota("mate"))
}
