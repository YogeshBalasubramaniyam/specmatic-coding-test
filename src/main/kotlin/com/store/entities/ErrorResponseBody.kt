package com.store.entities

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

/**
 * 
 * @param timestamp 
 * @param status 
 * @param error 
 * @param path 
 */
data class ErrorResponseBody(

    @Schema(example = "null", description = "")
    @get:JsonProperty("timestamp") val timestamp: java.time.OffsetDateTime? = null,

    @Schema(example = "null", description = "")
    @get:JsonProperty("status") val status: kotlin.Int? = null,

    @Schema(example = "null", description = "")
    @get:JsonProperty("error") val error: kotlin.String? = null,

    @Schema(example = "null", description = "")
    @get:JsonProperty("path") val path: kotlin.String? = null
) {

}

