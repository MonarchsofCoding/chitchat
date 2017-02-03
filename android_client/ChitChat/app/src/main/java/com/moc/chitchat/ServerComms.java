package com.moc.chitchat;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.concurrent.Semaphore;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;

import javax.inject.Singleton;
import dagger.Module;

/**
 * Created by aakyo on 26/01/2017.
 *
 * Alpha Server Communication class.
 * All output is served with console output
 */

public class ServerComms {

    private URL serverURL;
    private HttpURLConnection serverConn;

    private JSONObject currentJSON;

    private Semaphore taskLock = new Semaphore(0);

    @Inject
    public ServerComms(String URL) throws Exception {
        serverURL = new URL(URL);
        serverConn = (HttpURLConnection) serverURL.openConnection();
        serverConn.setDoOutput(true);
        serverConn.setConnectTimeout(10000);
        serverConn.setReadTimeout(10000);
        serverConn.setRequestProperty("Content-Type", "application/json");
    }

    public boolean setRequestType(String reqType) throws ProtocolException {
        if (reqType.equals("GET") || reqType.equals("POST")) {
            serverConn.setRequestMethod(reqType);
            return true;
        } else {
            return false;
        }
    }

    public void requestWithJSON(JSONObject toSend) throws Exception {
        currentJSON = toSend;
        System.out.println("Executing HTTP Worker.");
        HTTPWorker postWorker = new HTTPWorker();
        postWorker.execute();
        taskLock.acquire();
        System.out.println("Request with a JSON object processed.");
    }


    private class HTTPWorker extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                serverConn.connect();
                OutputStreamWriter requestStream = new OutputStreamWriter(serverConn.getOutputStream());
                requestStream.write(currentJSON.toString());
                requestStream.close();

                System.out.println("Server response: " + serverConn.getResponseCode());
                BufferedReader responseStream = new BufferedReader(new InputStreamReader(serverConn.getInputStream(), "utf-8"));
                StringBuilder responseBuilder = new StringBuilder();
                String line;
                while ((line = responseStream.readLine()) != null) {
                    responseBuilder.append(line + "\n");
                }
                requestStream.close();
                serverConn.disconnect();
                System.out.println("Server response: " + responseBuilder.toString());
            } catch (Exception e) {
                System.out.println(e.getStackTrace());
            }
            currentJSON = null;
            taskLock.release();
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
}
