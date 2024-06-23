package com.store.entities

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import java.io.IOException


data class Product(
    val id: Int,

    @JsonDeserialize(using = ForceStringDeserializer::class)
    val name: String,

    val type: ProductType,

    val inventory: Int
)

data class ProductId(
    val id: Int
)

data class ProductDetails @JsonCreator constructor(
    @JsonProperty("name")
    @JsonDeserialize(using = ForceStringDeserializer::class)
    val name: String,

    @JsonProperty("type") val type: ProductType,

    @JsonProperty("inventory") val inventory: Int
)

enum class ProductType {
    book,
    food,
    gadget,
    other
}

class ForceStringDeserializer : JsonDeserializer<String>() {
    @Throws(IOException::class)
    override fun deserialize(
        jsonParser: JsonParser, deserializationContext: DeserializationContext
    ): String {
        if (jsonParser.currentToken != JsonToken.VALUE_STRING) {
            deserializationContext.reportWrongTokenException(
                String::class.java, JsonToken.VALUE_STRING,
                "Attempted to parse token %s to string",
                jsonParser.currentToken
            )
        }
        return jsonParser.valueAsString
    }
}