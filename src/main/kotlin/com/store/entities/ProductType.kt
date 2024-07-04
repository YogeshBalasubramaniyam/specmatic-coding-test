package com.store.entities

import com.fasterxml.jackson.annotation.JsonProperty

/**
* 
* Values: book,food,gadget,other
*/
enum class ProductType(val value: kotlin.String) {

    @JsonProperty("book") book("book"),
    @JsonProperty("food") food("food"),
    @JsonProperty("gadget") gadget("gadget"),
    @JsonProperty("other") other("other")
}

