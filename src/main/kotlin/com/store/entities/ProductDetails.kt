package com.store.entities

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.store.entities.deserializers.ForceBigDecimalDeserializer
import com.store.entities.deserializers.ForceIntegerDeserializer
import com.store.entities.deserializers.ForceStringDeserializer
import com.store.entities.deserializers.ProductTypeDeserializer
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.Valid
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotEmpty

/**
 * 
 * @param name 
 * @param type 
 * @param inventory 
 * @param cost 
 */
data class ProductDetails(

    @Schema(example = "null", required = true, description = "")
    @get:NotEmpty(message = "Product name must not be empty")
    @get:JsonProperty("name", required = true)
    @JsonDeserialize(using = ForceStringDeserializer::class)
    val name: kotlin.String,

    @field:Valid
    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("type", required = true)
    @JsonDeserialize(using = ProductTypeDeserializer::class)
    val type: ProductType,

    @get:Min(1, message = "Inventory must be between 1 and 9999")
    @get:Max(9999, message = "Inventory must be between 1 and 9999")
    @Schema(example = "null", required = true, description = "")
    @JsonDeserialize(using = ForceIntegerDeserializer::class)
    @get:JsonProperty("inventory", required = true)
    val inventory: kotlin.Int,

    @Schema(example = "null", description = "")
    @get:JsonProperty("cost")
    @JsonDeserialize(using = ForceBigDecimalDeserializer::class)
    val cost: java.math.BigDecimal = (-1).toBigDecimal()
) {

}
