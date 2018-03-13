package com.in.fujitsu.pricing.storage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.in.fujitsu.pricing.storage.entity.StorageBackupInfo;

@Repository
public interface StorageBackupRepository extends JpaRepository<StorageBackupInfo, Long> {

}