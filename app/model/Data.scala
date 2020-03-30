package model

object Data {

  def dataToGenerate = List(
    ProductData(None, "IMac", "Apple pc", ProductType.Laptop, BigDecimal(2000), "Apple", None),
    ProductData(None, "Macbook pro", "Apple laptop", ProductType.Laptop, BigDecimal(2500), "Apple", None)
  )
}
