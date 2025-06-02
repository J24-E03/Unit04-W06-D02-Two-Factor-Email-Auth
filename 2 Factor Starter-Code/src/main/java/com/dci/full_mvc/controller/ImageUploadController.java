package com.dci.full_mvc.controller;



import com.dci.full_mvc.model.ImageMetaData;
import com.dci.full_mvc.repository.ImageMetaDataRepository;
import com.dci.full_mvc.service.ImageUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ImageUploadController {

    private final ImageMetaDataRepository imageMetaDataRepository;
    private final ImageUploadService imageUploadService;

    @GetMapping("/upload")
    public String showUploadForm() {
        return "upload/upload-form";
    }

    @PostMapping("/upload")
    public String handleUpload(@RequestParam("image") MultipartFile file, Model model) {
        try {
            Map<String, String> uploadData = imageUploadService.uploadImage(file);

            String imageUrl = uploadData.get("url");
            String publicId = uploadData.get("publicId");

            ImageMetaData imageMetaData = new ImageMetaData();
            imageMetaData.setPath(imageUrl);
            imageMetaData.setPublicId(publicId);
            imageMetaDataRepository.save(imageMetaData);

            model.addAttribute("imageUrl", imageUrl);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", "Upload failed: " + e.getMessage());
        } catch (IOException e) {
            model.addAttribute("error", "An error occurred while uploading the file.");
        } catch (Exception e) {
            model.addAttribute("error", "Unexpected error: " + e.getMessage());
        }
        return "upload/upload-result";
    }

    @PostMapping("/delete")
    public String handleDelete(@RequestParam("id") Long id, Model model) {
        ImageMetaData metaData = imageMetaDataRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Image not found"));

        try {
            imageUploadService.deleteImage(metaData.getPublicId());
            imageMetaDataRepository.deleteById(id);
            model.addAttribute("message", "Image deleted successfully");
        } catch (IOException e) {
            model.addAttribute("error", "Delete failed: " + e.getMessage());
        }

        return "upload/delete-result"; // You can make a simple confirmation page
    }


}
