package com.store.entities.deserializers

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import java.io.IOException

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