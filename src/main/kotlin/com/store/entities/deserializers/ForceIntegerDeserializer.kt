package com.store.entities.deserializers

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import java.io.IOException

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