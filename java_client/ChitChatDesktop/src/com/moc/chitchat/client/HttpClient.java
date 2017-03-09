package com.moc.chitchat.client;

import com.moc.chitchat.application.Configuration;

import java.io.IOException;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONString;
import org.springframework.stereotype.Component;

/**
 * HttpClient provides authenticated (when set) access to HTTP APIs.
 */
@Component
public class HttpClient {

    private Configuration configuration;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


    public HttpClient(Configuration configuration) {
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
    public Response post(String uri, JSONString object) throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, object.toJSONString());
        if (this.configuration.getLoggedInUser() != null) {
            Request request = new Request.Builder()
                    .url(this.configuration.getBackendAddress() + uri)
                    .header("accept", "application/json")
                    .header("Content-Type", "application/json")
                    .header("Authorization", "bearer " + this.configuration.getLoggedInUser().getAuthToken())
                    .post(body)
                    .build();

            Response response = client.newCall(request).execute();
            return response;
        }
        Request request = new Request.Builder()
                .url(this.configuration.getBackendAddress() + uri)
                .header("accept", "application/json")
                .header("Content-Type", "application/json")
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        return response;

    }

    /**
     * Attempts to get the object from the server via Http request.
     *
     * @param uri   adds the location of the url
     * @param query - query the server. Can have multiple quries
     * @return response in Unirest
     * @throws IOException - If invalid
     */
    public Response get(String uri, Map<String, Object> query) throws IOException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(this.configuration.getBackendAddress()).newBuilder();

        // Adding paths to the segment
        if (uri.contains("/")) {
            String[] pathsToAdd = uri.split("/");
            for (int i = 0; i < pathsToAdd.length; i++) {
                urlBuilder.addPathSegment(pathsToAdd[i]);
            }
        } else {
            urlBuilder.addPathSegment(uri);
        }

        for (Map.Entry<String, Object> entry : query.entrySet()) {
            urlBuilder.addQueryParameter(entry.getKey(), entry.getValue().toString());
        }

        Request request;
        if (this.configuration.getLoggedInUser() != null) {
            request = new Request.Builder()
                    .url(urlBuilder.build().toString())
                    .header("Accept", "application/json")
                    .addHeader("Authorization", "bearer "
                            + this.configuration.getLoggedInUser().getAuthToken()).build();
        } else {
            request = new Request.Builder()
                    .url(urlBuilder.build().toString()).build();
        }

        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(request).execute();

        return response;
    }
}
