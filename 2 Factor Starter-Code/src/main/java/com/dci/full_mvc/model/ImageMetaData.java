package com.dci.full_mvc.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "image_meta_data")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ImageMetaData implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String path; // The secure_url
    private String publicId; // <-- ADD THIS

    @OneToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;
}



/*
    @Entity
@Table(name = "image_meta_data")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ImageMetaData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String path;         // The secure_url
    private String publicId;     // Cloudinary public ID
    private String fileName;     // Original filename
    private String format;       // Image format (jpg, png, etc.)
    private Long fileSize;       // Size in bytes
    private Integer width;       // Image width in pixels
    private Integer height;      // Image height in pixels

    @Column(length = 512)
    private String description;  // Optional description of the image

    private String contentType;  // MIME type of the image

    @CreationTimestamp
    private LocalDateTime uploadedAt;  // When the image was uploaded

    private String uploadedBy;   // User who uploaded the image

    private String transformationString;  // Cloudinary transformation parameters if any

    @Column(name = "alt_text")
    private String altText;      // Alternative text for accessibility

    private String folder;       // Cloudinary folder path if using folders

    @Column(name = "is_active")
    private Boolean isActive = true;  // Soft delete flag
}

**/
