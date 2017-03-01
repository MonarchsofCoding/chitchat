package com.moc.chitchat.client;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import com.moc.chitchat.application.Configuration;

import java.util.Map;

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
     * Attempts to post the object to the server via Http request.
     *
     * @param uri    adds the location of the url
     * @param object adds the object to JSONString to the body
     * @return response in Unirest
     * @throws UnirestException - If invalid post with the httpClient
     */
    public HttpResponse<JsonNode> post(String uri, JSONString object) throws UnirestException {
        if (this.configuration.getLoggedInUser() != null) {
            return Unirest
                    .post(configuration.getBackendAddress() + uri)
                    .header("accept", "application/json")
                    .header("Content-Type", "application/json")
                    .header("Authorization", "bearer " + this.configuration.getLoggedInUser().getAuthToken())
                    .body(object.toJSONString())
                    .asJson()
                    ;
        }
        return Unirest
                .post(configuration.getBackendAddress() + uri)
                .header("accept", "application/json")
                .header("Content-Type", "application/json")
                .body(object.toJSONString())
                .asJson()
                ;
    }

    /**
     * Attempts to get the object from the server via Http request.
     *
     * @param uri   adds the location of the url
     * @param query - query the server. Can have multiple quries
     * @return response in Unirest
     * @throws UnirestException - If invalid e.g. can't connect to server
     */
    public HttpResponse<JsonNode> get(String uri, Map<String, Object> query) throws UnirestException {
        GetRequest req = Unirest
                .get(configuration.getBackendAddress() + uri)
                .header("accept", "application/json")
                .header("Content-Type", "application/json");

        if (this.configuration.getLoggedInUser() != null) {
            req.header("Authorization", "bearer " + this.configuration.getLoggedInUser().getAuthToken());
        }

        for (Map.Entry<String, Object> entry : query.entrySet()) {
            req.queryString(entry.getKey(), entry.getValue());
        }

        return req.asJson();
    }
}
