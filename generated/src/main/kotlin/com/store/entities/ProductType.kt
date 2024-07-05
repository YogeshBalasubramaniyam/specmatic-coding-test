package com.store.entities

import java.util.Objects
import com.fasterxml.jackson.annotation.JsonValue
import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.DecimalMax
import javax.validation.constraints.DecimalMin
import javax.validation.constraints.Email
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size
import javax.validation.Valid
import io.swagger.v3.oas.annotations.media.Schema

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

