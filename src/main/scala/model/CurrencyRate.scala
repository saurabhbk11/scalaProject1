package model

import java.time.LocalDate

case class CurrencyRate (
                          date : LocalDate,
                          currencyMap : Map[String,Option[Double]]
                        )
