package com.in.fujitsu.pricing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.in.fujitsu.pricing.entity.UserInfo;

/**
 * @author Maninder
 *
 */
@Repository
public interface UserRepository extends JpaRepository<UserInfo, Long> {

	public UserInfo findByUserName(String userName);

	public UserInfo findByEmailId(String emailId);

	public UserInfo findByUserId(Integer userId);


	@Query(value = "select * from ACCESS_MASTER where  status = 'Pending'  ", nativeQuery = true)
	public List<UserInfo> findAllPendingUserRequests();

	public UserInfo findByToken(String token);

	public List<UserInfo> findByStatus(String status);

}
