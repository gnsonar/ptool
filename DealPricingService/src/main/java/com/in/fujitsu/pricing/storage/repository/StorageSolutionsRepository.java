
package com.in.fujitsu.pricing.storage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.in.fujitsu.pricing.storage.entity.StorageSolutionsInfo;;


@Repository
public interface StorageSolutionsRepository extends JpaRepository<StorageSolutionsInfo, Long> {

}
