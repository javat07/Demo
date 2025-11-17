package com.example.demo.service;

import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class FirebaseLogin {

    private static final String API_KEY = "AIzaSyBWCoYSnU3QBWjaqgKJo_GAAfSAXayhnaM";

    public static JSONObject login(String email, String password) {
        try {
            String json = "{ \"email\":\"" + email + "\", \"password\":\"" + password + "\", \"returnSecureToken\":true }";

            URL url = new URL("https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=" + API_KEY);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            os.write(json.getBytes());
            os.flush();
            os.close();

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line, output = "";

            while ((line = br.readLine()) != null) output += line;

            JSONObject obj = new JSONObject(output);

            return obj;   // return full Firebase response

        } catch (Exception e) {
            return null;
        }
    }
}
