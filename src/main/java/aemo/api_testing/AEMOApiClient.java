package aemo.api_testing;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class AEMOApiClient {
    private static final String BASE_URL = "https://developer-portal-prd.aemo.com.au/api-docs";
    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;

    public AEMOApiClient() {
        this.httpClient = new OkHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public String getEnergyData(String endpoint) throws Exception {
        Request request = new Request.Builder()
                .url(BASE_URL + endpoint)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            return response.body().string();
        }
    }

    public <T> T parseJson(String json, Class<T> clazz) throws Exception {
        return objectMapper.readValue(json, clazz);
    }

    public static void main(String[] args) {
        AEMOApiClient client = new AEMOApiClient();
        try {
            String jsonData = client.getEnergyData("/v1/energy-data");
            System.out.println("Raw JSON Data: " + jsonData);

            // Example: Parse JSON into a custom Java object
            // MyDataClass data = client.parseJson(jsonData, MyDataClass.class);
            // System.out.println("Parsed Data: " + data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
