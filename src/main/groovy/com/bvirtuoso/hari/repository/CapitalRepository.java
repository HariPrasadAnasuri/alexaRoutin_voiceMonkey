package com.bvirtuoso.hari.repository;

import com.bvirtuoso.hari.model.jpa.CapitalEntity;
import com.bvirtuoso.hari.model.jpa.CountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CapitalRepository extends JpaRepository<CapitalEntity, Long> {

  List<CapitalEntity> findByCountry(CountryEntity countryEntity);
  CapitalEntity findByCapital(String capital);
}
