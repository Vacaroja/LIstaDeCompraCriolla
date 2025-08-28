package com.ccc.listadecompracriolla.Core.clases

data class ClientList(
    val id : Int? = 0,
    var name: String? = "",
    var presupuesto: String = ""
)

data class ClientProductList(
    val idClient: Int,
    val idProduct: Int
)

data class Product(
    val id: Int = 0,
    val name: String = "",
    var cant: Float = 1f,
    var price: Float = 0f,
    var checked: Boolean = false,
    var nota: String = "",
    var medida : String = "",
    val client: Int? = null
    )