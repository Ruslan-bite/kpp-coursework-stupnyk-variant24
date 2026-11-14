package ua.edu.nupp.kpp.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpMuseumExhibitsClient {

    private static final String BASE = "http://localhost:8080/museum-exhibits-server";

    private final HttpClient httpClient;
    private final ObjectMapper mapper;

    public HttpMuseumExhibitsClient() {
        this.httpClient = HttpClient.newHttpClient();
        this.mapper = new ObjectMapper();
    }

    private String enc(String value) {
        return URLEncoder.encode(value == null ? "" : value, StandardCharsets.UTF_8);
    }

    private static String nz(String value) {
        return value == null ? "" : value;
    }

    // Експонати
    public List<Exhibit> getExhibits() throws IOException, InterruptedException {
        return Arrays.asList(mapper.readValue(get("/exhibits"), Exhibit[].class));
    }

    public void addExhibit(Exhibit e) throws IOException, InterruptedException {
        post("/exhibits", exhibitParams("add", e));
    }

    public void updateExhibit(Exhibit e) throws IOException, InterruptedException {
        Map<String, String> params = exhibitParams("update", e);
        params.put("id", String.valueOf(e.getId()));
        post("/exhibits", params);
    }

    public void deleteExhibit(long id) throws IOException, InterruptedException {
        post("/exhibits", Map.of("action", "delete", "id", String.valueOf(id)));
    }

    // Огляди
    public List<Inspection> getInspections() throws IOException, InterruptedException {
        return Arrays.asList(mapper.readValue(get("/inspections"), Inspection[].class));
    }

    public void addInspection(Inspection i) throws IOException, InterruptedException {
        post("/inspections", inspectionParams("add", i));
    }

    public void updateInspection(Inspection i) throws IOException, InterruptedException {
        Map<String, String> params = inspectionParams("update", i);
        params.put("id", String.valueOf(i.getId()));
        post("/inspections", params);
    }

    public void deleteInspection(long id) throws IOException, InterruptedException {
        post("/inspections", Map.of("action", "delete", "id", String.valueOf(id)));
    }

    // Фахівці
    public List<Specialist> getSpecialists() throws IOException, InterruptedException {
        return Arrays.asList(mapper.readValue(get("/specialists"), Specialist[].class));
    }

    public void addSpecialist(Specialist s) throws IOException, InterruptedException {
        post("/specialists", Map.of(
                "action", "add",
                "fullName", nz(s.getFullName()),
                "position", nz(s.getPosition()),
                "specialization", nz(s.getSpecialization())));
    }

    public void updateSpecialist(Specialist s) throws IOException, InterruptedException {
        post("/specialists", Map.of(
                "action", "update",
                "id", String.valueOf(s.getId()),
                "fullName", nz(s.getFullName()),
                "position", nz(s.getPosition()),
                "specialization", nz(s.getSpecialization())));
    }

    public void deleteSpecialist(long id) throws IOException, InterruptedException {
        post("/specialists", Map.of("action", "delete", "id", String.valueOf(id)));
    }

    // Заявки на консервацію
    public List<ConservationRequest> getRequests() throws IOException, InterruptedException {
        return Arrays.asList(mapper.readValue(get("/requests"), ConservationRequest[].class));
    }

    public void addRequest(long inspectionId, long specialistId, String description)
            throws IOException, InterruptedException {
        post("/requests", Map.of(
                "action", "add",
                "inspectionId", String.valueOf(inspectionId),
                "specialistId", String.valueOf(specialistId),
                "description", nz(description)));
    }

    public void updateRequest(long id, long inspectionId, long specialistId, String description)
            throws IOException, InterruptedException {
        post("/requests", Map.of(
                "action", "update",
                "id", String.valueOf(id),
                "inspectionId", String.valueOf(inspectionId),
                "specialistId", String.valueOf(specialistId),
                "description", nz(description)));
    }

    public void changeRequestState(long id, ConservationRequestState state)
            throws IOException, InterruptedException {
        post("/requests", Map.of(
                "action", "transition",
                "id", String.valueOf(id),
                "state", state.name()));
    }

    public void deleteRequest(long id) throws IOException, InterruptedException {
        post("/requests", Map.of("action", "delete", "id", String.valueOf(id)));
    }

    // Допоміжні параметри
    private Map<String, String> exhibitParams(String action, Exhibit e) {
        Map<String, String> params = new HashMap<>();
        params.put("action", action);
        params.put("inventoryCode", nz(e.getInventoryCode()));
        params.put("name", nz(e.getName()));
        params.put("material", nz(e.getMaterial()));
        params.put("hall", nz(e.getHall()));
        params.put("materialSensitivity", String.valueOf(e.getMaterialSensitivity()));
        params.put("exhibitionSignificance", String.valueOf(e.getExhibitionSignificance()));
        return params;
    }

    private Map<String, String> inspectionParams(String action, Inspection i) {
        Map<String, String> params = new HashMap<>();
        params.put("action", action);
        params.put("exhibitId", String.valueOf(i.getExhibitId()));
        params.put("inspectionDate", nz(i.getInspectionDate()));
        params.put("damageSeverity", String.valueOf(i.getDamageSeverity()));
        params.put("notes", nz(i.getNotes()));
        return params;
    }

    // HTTP
    private String get(String endpoint) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE + endpoint))
                .GET()
                .build();
        return send(request);
    }

    private String post(String endpoint, Map<String, String> params)
            throws IOException, InterruptedException {
        StringBuilder body = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (!first) {
                body.append("&");
            }
            body.append(enc(entry.getKey()))
                    .append("=")
                    .append(enc(entry.getValue()));
            first = false;
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE + endpoint))
                .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .POST(HttpRequest.BodyPublishers.ofString(body.toString(), StandardCharsets.UTF_8))
                .build();
        return send(request);
    }

    private String send(HttpRequest request) throws IOException, InterruptedException {
        HttpResponse<String> response = httpClient.send(request,
                HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        if (response.statusCode() >= 400) {
            throw new IOException("Помилка запиту: HTTP " + response.statusCode() + "\n"
                    + response.body());
        }
        return response.body();
    }
}