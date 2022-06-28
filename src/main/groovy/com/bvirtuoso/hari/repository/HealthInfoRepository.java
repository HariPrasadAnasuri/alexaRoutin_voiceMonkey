package com.bvirtuoso.hari.repository;

import com.bvirtuoso.hari.model.jpa.HealthInfoJpa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface HealthInfoRepository extends JpaRepository<HealthInfoJpa, Long> {
    List<HealthInfoJpa> findByNameAndLocalDate(String name, LocalDate localDate);
}
