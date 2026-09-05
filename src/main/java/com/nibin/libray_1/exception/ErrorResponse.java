package com.nibin.libray_1.exception;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(name = "ErrorResponse", description = "Returned by every failing request")
public record ErrorResponse(

        @Schema(example = "2026-09-05T08:39:58.120869900Z")
        Instant timestamp,

        @Schema(description = "Matches the HTTP status code", example = "404")
        int status,

        @Schema(example = "Book not found: 7")
        String message) {

    public static ErrorResponse of(int status, String message) {
        return new ErrorResponse(Instant.now(), status, message);
    }
}
