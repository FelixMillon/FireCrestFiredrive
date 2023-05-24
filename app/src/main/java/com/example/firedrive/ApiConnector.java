package com.example.firedrive;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiConnector {
    public static void fetchDataFromURL(String urlString, final OnDataFetchedListener listener) {
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                String urlString = params[0];
                String result = "";

                HttpURLConnection urlConnection = null;
                BufferedReader reader = null;
                StringBuilder stringBuilder = new StringBuilder();

                try {
                    URL url = new URL(urlString);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setDoInput(true);
                    urlConnection.setConnectTimeout(20000);
                    urlConnection.connect();

                    InputStream inputStream = urlConnection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(inputStream));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line);
                    }

                    result = stringBuilder.toString();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                if (listener != null) {
                    listener.onDataFetched(result);
                }
            }
        }.execute(urlString);
    }

    public interface OnDataFetchedListener {
        void onDataFetched(String data);
    }
}
