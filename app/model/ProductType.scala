package model

import model.`enum`.SlickSqlEnumeration

object ProductType extends SlickSqlEnumeration {

  type ProductType = Value
  val Laptop = Value(1)
  val SmartPhone = Value(2)
  val OrgTechnique = Value(3)
  val PC = Value(4)

}

