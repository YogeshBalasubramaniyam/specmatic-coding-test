package com.store.entities

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import javax.validation.Valid
import javax.validation.constraints.Max
import javax.validation.constraints.Min

/**
 * 
 * @param id 
 * @param name 
 * @param type 
 * @param inventory 
 * @param cost 
 */
data class Product(

    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("id", required = true) val id: kotlin.Int,

    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("name", required = true) val name: kotlin.String,

    @field:Valid
    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("type", required = true) val type: ProductType,

    @get:Min(1)
    @get:Max(9999)
    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("inventory", required = true) val inventory: kotlin.Int,

    @Schema(example = "null", description = "")
    @get:JsonProperty("cost") val cost: java.math.BigDecimal? = null
) {

}

