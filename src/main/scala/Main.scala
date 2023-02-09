import java.io.File
import java.time.LocalDate
import scala.io.Source


object Main {
  def main(args: Array[String]): Unit = {
  }
  case class currencyRate (date: String,rate: String)
  val filepath = "src/data/eurofxref-hist.csv"

  //var hashMap<LocalDate,hashMap<String,BigDecimal>> currencyRateMap = new hashMap<LocalDate,hashMap<String,BigDecimal>>()  // =
  val file = new File(filepath)
  val fileCon = Source.fromFile(file).getLines
  println(fileCon.length)


  val header = Source.fromFile(file).getLines.take(1).next().split(",").tail.toList
  println(header)

  val mapTry1 = Source.fromFile(file).getLines.map(_.split(",")).drop(1).toList
  println(mapTry1(0).mkString)
  println(mapTry1(0).head)
  println(mapTry1(0).tail.toList)

  val curr = mapTry1(0).tail.toList

  val bList = curr.map(_.toString.toDoubleOption)

  val m = (header zip bList).toMap
  println("map### "+m)
  val jpy = getDouble(m.get("JPY"))

  val usd = getDouble(m.get("USD"))
  println("sum  "+(usd+jpy))

  def getDouble(curr : Option[Option[Double]]): Double = {
    val cur = curr.getOrElse(0)
    val value: Double = cur match {
      case None => 0 //Or handle the lack of a value another way: throw an error, etc.
      case Some(s: Double) => s //return the string to set your value
    }
    return  value
  }


  val j = m.get("USD").getOrElse(0)
  //println("JPY "+dd)
 //val kj = k + j

  println("bList-> "+bList)
  //val mapCR = bList(0).getOrElse(0) + bList(1).getOrElse(0)
  val b1 = BigDecimal(curr(0))
  val b2 = BigDecimal(curr(1))

  //println("sum -> "+mapCR)
  println("sum2 -> "+(b1+b2))

  //mapTry1(0).tail.foreach(s => println(s))

  val format = new java.text.SimpleDateFormat("yyyy-MM-dd")
  val date1 = LocalDate.parse("2013-07-06")
  val date2 = LocalDate.parse("2014-07-06")
  println("date-> "+date1)
  println("date isAfter-> "+date2.isAfter(date1))

  val mapMut = scala.collection.mutable.Map[LocalDate, String]()

  mapMut += (date1 -> "JAN")

  println(mapMut)

  def open(path: String) = new File(path)

  implicit class DateFile(file: File) {
    def read() = Source.fromFile(file).getLines
  }

  val readData = open(filepath).read()
  val hed = readData.take(1).next().split(",").tail.toList
  println("readData-> "+hed)



  for(a <- 0 to mapTry1(0).length){
    //println(mapTry1(a).mkString)
  }
/*
val header = fileCon.toList
for(int i<-0 until 10 ){
println(header[i])
}
  def open(path: String) = new File(path)

  implicit class DateFile(file:File){
    def read() = Source.fromFile(file).getLines
  }
  val readData = open(filepath).read()
  println(readData.length)
  */

}