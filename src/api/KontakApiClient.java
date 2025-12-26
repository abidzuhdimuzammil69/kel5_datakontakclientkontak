// File: src/api/KontakApiClient.java
package api;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import model.Kontak;

public class KontakApiClient {
    private static final String BASE_URL = "http://localhost/application-tier-php/public/kontak";
    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();

    public List<Kontak> findAll() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ApiResponse<List<Kontak>> apiResp = gson.fromJson(response.body(),
                new TypeToken<ApiResponse<List<Kontak>>>() {
                }.getType());
        if (!apiResp.success)
            throw new Exception(apiResp.message);
        return apiResp.data;
    }

    public void create(Kontak kontak) throws Exception {
        String json = gson.toJson(kontak);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        handleResponse(response);
    }

    public void update(Kontak kontak) throws Exception {
        var requestBody = new HashMap<String, Object>();
        requestBody.put("nama", kontak.getNama());
        requestBody.put("telepon", kontak.getTelepon());
        requestBody.put("email", kontak.getEmail() != null ? kontak.getEmail() : null);
        String json = gson.toJson(requestBody);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + kontak.getId()))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        handleResponse(response);
    }

    public void delete(int id) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id))
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        handleResponse(response);
    }

    private static class ApiResponse<T> {
        boolean success;
        T data;
        String message;
    }

    private void handleResponse(HttpResponse<String> response) throws Exception {
        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new RuntimeException("HTTP " + response.statusCode() + ": " + extractErrorMessage(response.body()));
        }
        ApiResponse<?> apiResp = gson.fromJson(response.body(), ApiResponse.class);
        if (!apiResp.success)
            throw new Exception(apiResp.message);
    }

    private String extractErrorMessage(String body) {
        try {
            ApiResponse<?> resp = gson.fromJson(body, ApiResponse.class);
            return resp.message != null ? resp.message : "Unknown server error";
        } catch (Exception e) {
            return "Server returned invalid response: " + body;
        }
    }
}