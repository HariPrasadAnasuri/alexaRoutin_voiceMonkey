package com.bvirtuoso.hari.repository;

import com.bvirtuoso.hari.model.jpa.GkQuestionsWithImagesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GkQuestionsWithImagesRepository extends JpaRepository<GkQuestionsWithImagesEntity, Long> {
  GkQuestionsWithImagesEntity findTopById(Long id);
  GkQuestionsWithImagesEntity findTop1ByProgress(Boolean progress);
}
