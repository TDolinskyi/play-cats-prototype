package model


object Property extends scala.Enumeration {
  type Property = Value
  val NAME = Value(1)
  val DEVICE_TYPE = Value(2)
  val PRICE = Value(3)
  val BRAND = Value(4)
  val ID = Value(5)
}
