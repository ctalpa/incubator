package com.ct.rxm.repository;

import com.ct.rxm.domain.JobHistory;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the JobHistory entity.
 */
public interface JobHistoryRepository extends JpaRepository<JobHistory,Long> {

}
