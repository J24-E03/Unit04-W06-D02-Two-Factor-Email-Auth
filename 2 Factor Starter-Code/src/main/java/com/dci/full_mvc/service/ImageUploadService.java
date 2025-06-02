package com.dci.full_mvc.service;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ImageUploadService {

    private final Cloudinary cloudinary;


    public Map<String, String> uploadImage(MultipartFile file) throws IOException {
        long maxFileSize = 5 * 1024 * 1024; // 5MB limit

        if (file.getSize() > maxFileSize) {
            throw new IllegalArgumentException("File size exceeds the limit of 5MB.");
        }

        Map<String, Object> options = ObjectUtils.asMap(
                "resource_type", "image",
                "allowed_formats", new String[]{"jpg", "png", "jpeg"}
        );

        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), options);

        String url = (String) uploadResult.get("secure_url");
        String publicId = (String) uploadResult.get("public_id");

        return Map.of(
                "url", url,
                "publicId", publicId
        );
    }

    public void deleteImage(String publicId) throws IOException {
        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    }

}
