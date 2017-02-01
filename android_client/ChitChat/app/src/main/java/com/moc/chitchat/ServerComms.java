package com.moc.chitchat;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ProtocolException;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

import javax.inject.Singleton;
import dagger.Module;

/**
 * Created by aakyo on 26/01/2017.
 *
 * Alpha Server Communication class. Nothing is set stone.
 * TODO Aydin: need to return the response code
 * TODO Aydin: need to test with an example
 */

@Module
@Singleton
public class ServerComms {

    private URL serverURL;
    private HttpsURLConnection serverConn;

    private JSONObject currentJSON;
    private String resultString;

    public ServerComms(String URL) throws Exception {
        serverURL = new URL(URL); //TODO Aydin: change when VJ runs the server.
        serverConn = (HttpsURLConnection) serverURL.openConnection();
        serverConn.setConnectTimeout(10000);
        serverConn.setReadTimeout(10000);
    }

    public boolean setRequestType(String reqType) throws ProtocolException {
        if (reqType == "GET" || reqType == "POST") {
            serverConn.setRequestMethod(reqType);
            return true;
        }
        else {
            return false;
        }
    }

    public JSONObject requestWithJSON(JSONObject toSend) throws Exception {
        resultString = "";
        currentJSON = toSend;
        ServerWorker postWorker = new ServerWorker();
        postWorker.execute();
        currentJSON = null;
        if (resultString != "") {
            return new JSONObject(resultString);
        }
        else {
            return null;
        }
    }

    private class ServerWorker extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... strings) {
            serverConn.setRequestProperty("Content-Type", "application/json");
            try {
                serverConn.connect();

                OutputStream requestStream = new BufferedOutputStream(serverConn.getOutputStream());
                requestStream.write(currentJSON.toString().getBytes());
                requestStream.flush();
                requestStream.close();

                BufferedReader responseStream = new BufferedReader(new InputStreamReader(serverConn.getInputStream()));
                StringBuilder responseBuilder = new StringBuilder();
                String line;
                while ((line = responseStream.readLine()) != null) {
                    responseBuilder.append(line + "\n");
                }
                requestStream.close();
                serverConn.disconnect();
                return responseBuilder.toString();
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
                return "EXCEPTION";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != "EXCEPTION") {
                resultString = result;
            }
            else {
                resultString = "";
            }
        }

    }
}

