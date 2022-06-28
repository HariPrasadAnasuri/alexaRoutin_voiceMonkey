package com.bvirtuoso.hari.repository;

import com.bvirtuoso.hari.model.jpa.DishInfoJpa;
import com.bvirtuoso.hari.model.jpa.HealthInfoJpa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface DishInfoRepository extends JpaRepository<DishInfoJpa, Long> {
    List<DishInfoJpa> findByLocalDateAndTimeOfDay(LocalDate localDate, String timeOfDay);
}
