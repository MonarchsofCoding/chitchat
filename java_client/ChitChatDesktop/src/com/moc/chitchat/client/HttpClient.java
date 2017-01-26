package com.moc.chitchat.client;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONString;
import org.springframework.stereotype.Component;

/**
 * HttpClient provides authenticated (when set) access to HTTP APIs.
 */
@Component
public class HttpClient {

    private String baseApiAddress;

    HttpClient() {
        this.baseApiAddress = "http://localhost"; // TODO: Set up global configuration
    }

    public HttpResponse<JsonNode> post(String uri, JSONString object) throws UnirestException {
        return Unirest
            .post(baseApiAddress + uri)
            .header("accept", "application/json")
            .body(object.toJSONString().getBytes())
            .asJson()
        ;
    }


}
