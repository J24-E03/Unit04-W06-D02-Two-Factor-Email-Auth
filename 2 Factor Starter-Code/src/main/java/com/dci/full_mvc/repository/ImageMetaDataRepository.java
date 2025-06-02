package com.dci.full_mvc.repository;


import com.dci.full_mvc.model.ImageMetaData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageMetaDataRepository extends JpaRepository<ImageMetaData, Long> {
}
