package com.bvirtuoso.hari.repository;

import com.bvirtuoso.hari.model.jpa.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import jakarta.transaction.Transactional;
import java.util.List;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

  List<MemberEntity> findByPerson(String person);

  @Modifying
  @Transactional
  @Query(value = "Backup to ?1", nativeQuery = true)
  int backupDB(String path);
}
