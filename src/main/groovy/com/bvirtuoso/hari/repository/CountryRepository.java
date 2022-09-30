package com.bvirtuoso.hari.repository;

import com.bvirtuoso.hari.model.jpa.CountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CountryRepository extends JpaRepository<CountryEntity, Long> {

  List<CountryEntity> findByCountry(String country);
}
