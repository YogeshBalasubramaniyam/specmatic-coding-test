package com.store.entities.deserializers

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.store.entities.ProductType
import java.io.IOException

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