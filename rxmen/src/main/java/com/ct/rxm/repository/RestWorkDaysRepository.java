package com.ct.rxm.repository;

import com.ct.rxm.domain.RestWorkDays;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the RestWorkDays entity.
 */
public interface RestWorkDaysRepository extends JpaRepository<RestWorkDays,Long> {

}
