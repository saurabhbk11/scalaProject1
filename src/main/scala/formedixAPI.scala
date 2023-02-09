import Main.{file, filepath}
import model.CurrencyRate

import java.io.File
import java.time.LocalDate
import scala.io.Source

object formedixAPI {
    def main(args: Array[String]): Unit = {
    }
    val filepath = "src/data/eurofxref-hist.csv"

    val file = new File(filepath)
    val header = Source.fromFile(file).getLines.take(1).next().split(",").tail.toList
    println(header)

    val data = Source.fromFile(file).getLines.map(_.split(",")).drop(1).toList

/*  def ppp() {
    for {
      line <- Source.fromFile(file).getLines.take(5).drop(1)
      values = line.split(",")
    } yield println("yee-> " + line)
  }
    val p = ppp()
  println("ppp-> "+p)*/
//    for(i <- 0 until 5){ //(data.length)
//      println(data(i).head)
//      val currencyExList = (data(i).tail.toList).map(_.toString.toDoubleOption)
//      val currencyIdExMap = (header zip currencyExList).toMap
//      CurrencyRate(data(i).head,currencyIdExMap)
//    }

  val result: Map[LocalDate, Map[String,Option[Double]]] = data.map(e => (stringToDate(e.head),(header zip (e.tail.toList).map(_.toString.toDoubleOption)).toMap )).toMap

  val curenRate: List[CurrencyRate] = data.map(e => CurrencyRate(stringToDate(e.head),(header zip (e.tail.toList).map(_.toString.toDoubleOption)).toMap )).toList

  println("currencRate "+curenRate.filter(data => data.date == stringToDate("2023-01-20"))) //ap(m => m.currencyMap.get("USD").getOrElse(0)).toList)
  val j  = result.get(stringToDate("2022-01-26")) //.get("INR").getOrElse(0)
  println("j => "+j)
//  val u : Double = result.get(stringToDate("2022-01-26")).get("USD").getOrElse(0)
//  val amount : Double = 10
//  val exc = amount.*(j)./(u)
//  println("Exchange Rate -> "+exc)
  //println(result.get("2022-01-26").get("JPY").getOrElse(0) + result.get("2022-01-26").get("JPY").getOrElse(0))

  val rangeData = getDataInRange(stringToDate("2023-01-20"),stringToDate("2023-01-26"),"USD")
  println("Range-> "+rangeData)
  val v = rangeData.sum/ rangeData.length
  println("After Range-> "+v)

  val currCon = currencyConversion(stringToDate("2023-01-20"),"USD","INR",10)
  println("Conversion-> "+currCon)

  val currConApp = currencyConversion(stringToDate("2023-01-20"), "USD", "INR", _)

  println("Conversion Applied-> " + currConApp(10))

  println("Conversion Applied 20-> " + currConApp(20))

  val m = (currencyConversion _).curried
  println("Conversion Applied 10->"+ m(stringToDate("2023-01-20"))("USD")("INR")(10))
  println("Conversion Applied 20->"+ m(stringToDate("2023-01-20"))("USD")("INR")(20))

  val dt = stringToDate(_)
  println("dt curr "+ dt("2023-01-20"))
  println("dt curr2 "+ dt("2024-01-20"))

  val maxRate = getHighestExchangeRate(stringToDate("2023-01-20"),stringToDate("2023-01-26"),"USD")
  println("mAX rATE-> "+maxRate)

  val maxRateH = getHigestHOF(getDataInRange,stringToDate("2023-01-20"), stringToDate("2023-01-26"), "USD")
  println("mAX rATE HOF-> " + maxRateH)


  val avgRate = getAverageExchangeRate(stringToDate("2023-01-20"), stringToDate("2023-01-26"), "USD")
  println("avg rATE-> " + avgRate)

  val avgRateH = getAvgExHOF(getDataInRange,getAvgOfListElement,stringToDate("2023-01-20"), stringToDate("2023-01-26"), "USD")
  println("avg rATE H-> " + avgRateH)

  val p = getAllCurrencyRate(stringToDate("2023-01-20"))

  def getAllCurrencyRate(date: LocalDate): Map[String,Option[Double]] = {
    return result.get(date).orNull
  }
  def getHighestExchangeRate(startDate: LocalDate,endDate: LocalDate,currencyId: String): Double = {
    val maxCurrencyExchangeRate = getDataInRange(startDate,endDate, currencyId).max
    println(maxCurrencyExchangeRate)
    return maxCurrencyExchangeRate
  }

  def getHigestHOF(f: (LocalDate,LocalDate,String) => List[Double],startDate: LocalDate,endDate: LocalDate,currencyId: String):Double={
    val m = f(startDate,endDate,currencyId).max
    return m
  }

  def getAverageExchangeRate(startDate: LocalDate,endDate: LocalDate,currencyId: String): Double={
    val avgCurrencyExchangeRate = getDataInRange(startDate,endDate, currencyId)
    val avg = avgCurrencyExchangeRate.reduce((a,b)=> a+b)/avgCurrencyExchangeRate.length
    return  avg
  }
  def getAvgExHOF(f: (LocalDate,LocalDate,String) => List[Double],g: (List[Double])=> Double,startDate: LocalDate,endDate: LocalDate,currencyId: String) : Double ={
    return  g(f(startDate,endDate,currencyId))
  }

  def getAvgOfListElement(list: List[Double]) : Double ={
    if(list.isEmpty){
      return 0
    }
    return list.sum/list.length
  }
  def stringToDate(s:String):LocalDate ={
    val format = new java.text.SimpleDateFormat("yyyy-MM-dd")
    if(!s.isEmpty){
      val dateValue = LocalDate.parse(s)
      return dateValue
    }
     return null
  }

  def currencyConversion(date: LocalDate,sourceCurrencyId: String,targetCurrencyId: String,amount : Double):Double={
    val sourceCurrency: Double = result.get(date).get(sourceCurrencyId).getOrElse(0)
    val targetCurrency: Double = result.get(date).get(targetCurrencyId).getOrElse(0)
    if(sourceCurrency == 0 || targetCurrency == 0){
      return 0
    }
    return amount.*(targetCurrency)./(sourceCurrency)
  }

  def getDataInRange(startDate: LocalDate,endDate: LocalDate,currencyId: String): List[Double] = {
    return result.filter(x => ( !x._1.isBefore(startDate) &&  !x._1.isAfter(endDate)))
      .map(r => r._2.get(currencyId).getOrElse(0)).toList.map(x => x match {
      case None => 0 //Or handle the lack of a value another way: throw an error, etc.
      case Some(s: Double) => s //return the string to set your value
    })
  }

}
