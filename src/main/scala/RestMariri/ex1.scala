package RestMariri

import com.cra.figaro.language.Flip
import com.cra.figaro.library.compound.If

object ex1 {
  //probabilitatea sa ninga este de 0.6, iar probabilitatea sa nu ninga este de 0.4
  val ninge = Flip(0.6)
  val nrzileNinge = 0
  val calitateSaptamana = If(nrzileNinge > 5, "prea multa ninsoare")
}