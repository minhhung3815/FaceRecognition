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

@RestController
@RequestMapping("/api/attendances")
public class AttendanceController {

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

            //Logic for face recognition

            Boolean isFaceRecognized = true; // Placeholder for actual face recognition logic
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
