package com.bvirtuoso.hari.repository;

import com.bvirtuoso.hari.model.jpa.GkQuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GkQuestionRepository extends JpaRepository<GkQuestionEntity, Long> {

  GkQuestionEntity findTopByStatus(Integer status);
  GkQuestionEntity findTopById(Long status);

}
