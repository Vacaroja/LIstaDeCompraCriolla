package com.ccc.listadecompracriolla.Core.clases

import com.ccc.listadecompracriolla.entities.ClientListEntity
import com.ccc.listadecompracriolla.entities.ProductEntity

data class ClientList(
    val id : Int? = 0,
    var name: String? = "",
    var presupuesto: String = ""
)

fun ClientList.ToClientListEntity(): ClientListEntity{
    return ClientListEntity(
        id = this.id,
        name = this.name.toString(),
        presupuesto = this.presupuesto
    )
}

data class ClientProductList(
    val idClient: Int,
    val idProduct: Int
)

data class Product(
    val id: Int? = null,
    val name: String = "",
    var cant: Float = 1f,
    var price: Float = 0f,
    var checked: Boolean = false,
    var nota: String = "",
    var medida : String = "",
    val client: Int? = null
    )

fun Product.ProductEntity(): ProductEntity{
    return ProductEntity(
        this.id,
        this.name,
        this.cant,
        this.price,
        this.checked,
        this.nota,
        this.medida,
        this.client
    )
}