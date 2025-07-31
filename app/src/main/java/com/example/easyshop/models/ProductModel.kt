package com.example.easyshop.models

data class ProductModel(
    var id : String = "",
    var title : String = "",
    var description : String = "",
    var price : String="",
    var actualPrice : String ="",
    var category : String = "",
    var images : List<String> = emptyList(),
    var otherDetails : Map<String,String> = mapOf()
)
