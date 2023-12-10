package com.bvirtuoso.hari.repository;

import com.bvirtuoso.hari.model.jpa.PersonAvailabilityEntity;
import com.bvirtuoso.hari.model.jpa.TvOnOffEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PersonAvailabilityRepository extends JpaRepository<PersonAvailabilityEntity, Long> {

  @Query(value = "select id, activity_time, availability from person_availability order by id desc limit 1", nativeQuery = true)
  PersonAvailabilityEntity getLastRow();

  @Query(value = "select id, activity_time, availability from person_availability order by id asc limit 1", nativeQuery = true)
  PersonAvailabilityEntity getFirstRow();

}
