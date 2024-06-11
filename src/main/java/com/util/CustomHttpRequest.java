package com.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class CustomHttpRequest {

    private static final String USER_AGENT = "Mozilla/5.0";

    private final String GET_URL;

    private static final String POST_URL = "https://api.weather.bom.gov.au/v1/locations/r7hgdpk/forecasts/daily";

    private static final String POST_PARAMS = "userName=Pankaj";

    private final String response;

    public CustomHttpRequest(String GET_URL) throws IOException {
        this.GET_URL = GET_URL;
        this.response = sendGET();
    }

    public String getResponse() {
        return response;
    }

    private String sendGET() throws IOException {
        URL obj = new URL(GET_URL);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);
        int responseCode = con.getResponseCode();
        System.out.println("GET Response Code :: " + responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            return Success(con);
        } else {
            System.out.println("GET request did not work.");
        }

        return null;
    }

    private static void sendPOST() throws IOException {
        URL obj = new URL(POST_URL);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);

        // For POST only - START
        con.setDoOutput(true);
        OutputStream os = con.getOutputStream();
        os.write(POST_PARAMS.getBytes());
        os.flush();
        os.close();
        // For POST only - END

        int responseCode = con.getResponseCode();
        System.out.println("POST Response Code :: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) { //success
            Success(con);
        } else {
            System.out.println("POST request did not work.");
        }
    }

    public static String Success(HttpURLConnection con) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        // print result
        return response.toString();
    }

}