package com.harsh.cms.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.harsh.cms.model.Complaint;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
	List<Complaint> findByStatus(String status);

	List<Complaint> findByCustomer_Id(Long customerId);

	List<Complaint> findByAssignedEmployee_Id(Long employeeId);

	List<Complaint> findByCreatedDateBetween(LocalDateTime start, LocalDateTime end);
}
