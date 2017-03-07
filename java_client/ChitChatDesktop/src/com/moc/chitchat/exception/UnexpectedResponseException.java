package com.moc.chitchat.exception;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import okhttp3.Response;

/**
 * UnexpectedResponseException is thrown when an unexpected status code occurs e.g. 500
 */
public class UnexpectedResponseException extends Exception {

    private Response response;

    public UnexpectedResponseException(Response response) {
        super(String.format("Unexpected Response code: %s", response.code()));
        this.response = response;
    }

    public Response getResponse() {
        return this.response;
    }

}
