package com.moc.chitchat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

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

    private int returnCode;

    private Semaphore taskLock = new Semaphore(0);

    @Inject
    public ServerComms(String URL) throws Exception {
        serverURL = new URL(URL);
    }

    public int requestWithJSON(JSONObject toSend, String reqType) throws Exception {
        returnCode = 0;
        currentJSON = toSend;

        System.out.println("Executing HTTP Worker.");

        serverConn = (HttpURLConnection) serverURL.openConnection();
        serverConn.setDoOutput(true);
        serverConn.setRequestMethod(reqType);
        serverConn.setConnectTimeout(10000);
        serverConn.setReadTimeout(10000);
        serverConn.setRequestProperty("Content-Type", "application/json");

        HTTPWorker postWorker = new HTTPWorker();
        postWorker.execute();

        taskLock.acquire();

        System.out.println("Request with a JSON object processed. Response code: " + returnCode);
        return returnCode;
    }

    private class HTTPWorker extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... params) {
            int serverResponseCode = 0;
            try {
                serverConn.connect();
                OutputStreamWriter requestStream = new OutputStreamWriter(serverConn.getOutputStream());
                requestStream.write(currentJSON.toString());
                requestStream.close();

                returnCode = serverConn.getResponseCode();
                System.out.println("Server response: " + returnCode);
                BufferedReader responseStream = new BufferedReader(new InputStreamReader(serverConn.getInputStream(), "utf-8"));
                StringBuilder responseBuilder = new StringBuilder();
                String line;
                while ((line = responseStream.readLine()) != null) {
                    responseBuilder.append(line + "\n");
                }
                requestStream.close();
                System.out.println("Server response: " + responseBuilder.toString());
            } catch (Exception e) {
                System.out.println(e.getStackTrace());
            }
            finally {
                serverConn.disconnect();
                currentJSON = null;
            }
            taskLock.release();
            return null;
        }
    }
}
