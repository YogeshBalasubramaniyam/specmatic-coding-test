package com.store.entities.deserializers

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import java.io.IOException

class ForceBigDecimalDeserializer : JsonDeserializer<java.math.BigDecimal>() {
    @Throws(IOException::class)
    override fun deserialize(
        jsonParser: JsonParser, deserializationContext: DeserializationContext
    ): java.math.BigDecimal {
        if (jsonParser.currentToken != JsonToken.VALUE_NUMBER_INT && jsonParser.currentToken != JsonToken.VALUE_NUMBER_FLOAT) {
            deserializationContext.reportWrongTokenException(
                java.math.BigDecimal::class.java, JsonToken.VALUE_NUMBER_FLOAT,
                "Expected a number for BigDecimal deserialization but got %s",
                jsonParser.currentToken
            )
        }
        return jsonParser.decimalValue
    }
}