package com.gamebudy.AuthService.exception.results;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DataResult<T> extends Result {
    private final T data;

    @JsonCreator
    public DataResult(@JsonProperty("data") T data,
                      @JsonProperty("success") boolean success,
                      @JsonProperty("message") String message) {
        super(success, message);
        this.data = data;
    }

    public DataResult(T data, boolean success) {
        super(success);
        this.data = data;
    }

    public T getData() {
        return this.data;
    }
}

