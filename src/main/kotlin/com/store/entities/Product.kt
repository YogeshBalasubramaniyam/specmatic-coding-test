package com.store.entities

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.JsonParseException
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

    val inventory: Int,

    val cost: Number = -1
)

data class ProductId(
    val id: Int
)

data class ProductDetails @JsonCreator constructor(
    @JsonProperty("name")
    @JsonDeserialize(using = ForceStringDeserializer::class)
    val name: String,

    @JsonProperty("type")
    @JsonDeserialize(using = ProductTypeDeserializer::class)
    val type: ProductType,

    @JsonProperty("inventory")
    @JsonDeserialize(using = ForceIntegerDeserializer::class)
    val inventory: Int,

    @JsonProperty("cost")
    @JsonDeserialize(using = ForceNumberDeserializer::class)
    val cost: Number = -1
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

class ForceIntegerDeserializer : JsonDeserializer<Int>() {
    @Throws(IOException::class)
    override fun deserialize(
        jsonParser: JsonParser, deserializationContext: DeserializationContext
    ): Int {
        if (jsonParser.currentToken != JsonToken.VALUE_NUMBER_INT) {
            deserializationContext.reportWrongTokenException(
                Int::class.java, JsonToken.VALUE_NUMBER_INT,
                "Attempted to parse token %s to integer",
                jsonParser.currentToken
            )
        }
        return jsonParser.valueAsInt
    }
}

class ForceNumberDeserializer : JsonDeserializer<Number>() {
    @Throws(IOException::class)
    override fun deserialize(
        jsonParser: JsonParser, deserializationContext: DeserializationContext
    ): Number {
        if (jsonParser.currentToken != JsonToken.VALUE_NUMBER_INT && jsonParser.currentToken != JsonToken.VALUE_NUMBER_FLOAT) {
            deserializationContext.reportWrongTokenException(
                Number::class.java, JsonToken.VALUE_NUMBER_INT,
                "Attempted to parse token %s to number",
                jsonParser.currentToken
            )
        }
        return jsonParser.valueAsDouble
    }
}

class ProductTypeDeserializer : JsonDeserializer<ProductType>() {
    @Throws(IOException::class)
    override fun deserialize(
        jsonParser: JsonParser, deserializationContext: DeserializationContext
    ): ProductType {
        val value = jsonParser.valueAsString
        return try {
            ProductType.valueOf(value)
        } catch (e: IllegalArgumentException) {
            throw JsonParseException(jsonParser, "Invalid value for ProductType: $value")
        }
    }
}
