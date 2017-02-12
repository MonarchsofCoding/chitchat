package com.moc.chitchat.client;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.moc.chitchat.application.Configuration;
import org.json.JSONString;
import org.springframework.stereotype.Component;

/**
 * HttpClient provides authenticated (when set) access to HTTP APIs.
 */
@Component
public class HttpClient {

    private Configuration configuration;


    HttpClient(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     *  Attempts to post the object to the server via Http request.
     * @param uri adds the location of the url
     * @param object adds the object to JSONString to the body
     * @return response in Unirest
     * @throws UnirestException - If invalid post with the httpClient
     */
    public HttpResponse<JsonNode> post(String uri, JSONString object) throws UnirestException {
        return Unirest
            .post(configuration.getBackendAddress() + uri)
            .header("accept", "application/json")
            .header("Content-Type", "application/json")
            .body(object.toJSONString())
            .asJson()
            ;
    }


}
