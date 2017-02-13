package com.moc.chitchat.exception;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;

/**
 * UnexpectedResponseException is thrown when an unexpected status code occurs e.g. 500
 */
public class UnexpectedResponseException extends Exception {

    private HttpResponse<JsonNode> response;

    public UnexpectedResponseException(HttpResponse<JsonNode> response) {
        super(String.format("Unexpected Response code: %s", response.getStatus()));
        this.response = response;
    }

    public HttpResponse<JsonNode> getResponse() {
        return this.response;
    }

}
