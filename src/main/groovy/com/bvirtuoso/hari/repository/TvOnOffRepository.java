package com.bvirtuoso.hari.repository;

import com.bvirtuoso.hari.model.jpa.TvOnOffEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TvOnOffRepository extends JpaRepository<TvOnOffEntity, Long> {

  @Query(value = "select id, activity_time, tv_status from tv_on_off order by id desc limit 1", nativeQuery = true)
  TvOnOffEntity getLastRow();

  @Query(value = "select id, activity_time, tv_status from tv_on_off order by id asc limit 1", nativeQuery = true)
  TvOnOffEntity getFirstRow();

}
