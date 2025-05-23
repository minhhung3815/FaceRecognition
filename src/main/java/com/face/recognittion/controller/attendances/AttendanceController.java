package com.face.recognittion.controller.attendances;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.face.recognittion.dto.ImageRequest;
import com.face.recognittion.service.ImageStorageService;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.face.recognittion.service.AttendanceService;
import com.face.recognittion.service.GridFsService;
import java.util.Base64;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.springframework.beans.factory.annotation.Value;

@RestController
@RequestMapping("/api/attendances")
public class AttendanceController {

    @Value("${zalo.api.key}")
    private String zaloApiKey;

    private final ImageStorageService imageStorageService;
    private final GridFsService gridFsService;
    private final AttendanceService attendanceService;

    public AttendanceController(ImageStorageService imageStorageService, GridFsService gridFsService, AttendanceService attendanceService) {
        this.imageStorageService = imageStorageService;
        this.gridFsService = gridFsService;
        this.attendanceService = attendanceService;
    }

    @GetMapping("/attendance")
    public ResponseEntity<?> getStudentAttendance(@RequestParam String param) {
        // Logic to check student attendance
        return ResponseEntity.ok(true);
    }

    @GetMapping("/scan-class-qr")
    public ResponseEntity<?> getStudentGrades(@RequestParam String param) {
        // Logic to scan class QR code
        return ResponseEntity.ok(true);
    }

    @GetMapping("/create-class-qr")
    public ResponseEntity<?> createClassQr(@RequestParam String param) {
        // Logic to create class QR code
        return ResponseEntity.ok(true);
    }

    @GetMapping("/verify-face")
    public ResponseEntity<?> verifyFace(@RequestParam String param) {
        // Logic to verify face
        return ResponseEntity.ok(true);
    }

    @PostMapping(value = "/store-face-image", consumes = "application/json")
    public ResponseEntity<?> storeFaceImage(@RequestBody ImageRequest imageRequest) {
        try {
            if (imageRequest.getImage() == null || imageRequest.getImage().isEmpty()) {
                return ResponseEntity.badRequest().body("No image data provided.");
            }

            String classDetailId = imageRequest.getClassDetailId();
            if (classDetailId == null || classDetailId.isEmpty()) {
                return ResponseEntity.badRequest().body("Class detail ID is required.");
            }
            String date = imageRequest.getDate();
            if (date == null || date.isEmpty()) {
                return ResponseEntity.badRequest().body("Student ID is required.");
            }

            // Decode Base64 image
            byte[] imageBytes = Base64.getDecoder().decode(imageRequest.getImage());

            // Save the image to a file on the local filesystem
            Path storagePath = Paths.get("face-images");
            if (!Files.exists(storagePath)) {
                Files.createDirectories(storagePath);
            }
            String fileName = System.currentTimeMillis() + ".jpg";
            Path filePath = storagePath.resolve(fileName);
            Files.write(filePath, imageBytes);

            // Load the image using OpenCV
            String imagePath = filePath.toString(); // Use the full path to the saved file
            Mat image = Imgcodecs.imread(imagePath);

            if (image.empty()) {
                return ResponseEntity.badRequest().body("Failed to load image.");
            }

            // Convert the image to a list of pixels
            byte[] imagePixels = new byte[(int) (image.total() * image.channels())];
            image.get(0, 0, imagePixels);

            // Prepare the API request payload
            Map<String, Object> data = new HashMap<>();
            data.put("uid", 0);
            data.put("image", imagePixels);
            data.put("url", null);

            // Convert the payload to JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonPayload = objectMapper.writeValueAsString(data);

            // Define the API endpoint and headers
            String endPoint = "https://api.zalo.ai/analyst_face_dev";
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(endPoint))
                    .header("Content-Type", "application/json")
                    .header("apikey", zaloApiKey) // Use the API key from the environment
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            // Send the API request
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Parse the API response
            Map<String, Object> resp = objectMapper.readValue(response.body(), Map.class);

            // Check the response for face recognition result
            Boolean isFaceRecognized = (Boolean) resp.get("recognized"); // Adjust key based on API response
            if (!isFaceRecognized) {
                return ResponseEntity.badRequest().body("Face not recognized.");
            }

            // Update attendance check-in
            attendanceService.updateCheckIn(imageRequest.getStudentId(), imageRequest.getClassDetailId(), imageRequest.getDate());

            return ResponseEntity.ok("Image stored successfully and attendance updated.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to store image: " + e.getMessage());
        }
    }

    @GetMapping("/get-face-image")
    public ResponseEntity<?> getFaceImage(@RequestParam String fileId) {
        try {
            GridFSFile file = gridFsService.getFile(fileId);
            if (file == null) {
                return ResponseEntity.notFound().build();
            }

            byte[] fileData = gridFsService.downloadFile(fileId);
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=\"" + file.getFilename() + "\"")
                    .body(fileData);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to retrieve image: " + e.getMessage());
        }
    }
}
