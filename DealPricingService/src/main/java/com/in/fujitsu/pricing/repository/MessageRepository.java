package com.in.fujitsu.pricing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.in.fujitsu.pricing.entity.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

	public List<Message> findTop5ByOrderByCreationDateDesc();
}
