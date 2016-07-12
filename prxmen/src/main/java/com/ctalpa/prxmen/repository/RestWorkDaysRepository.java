package com.ctalpa.prxmen.repository;

import com.ctalpa.prxmen.domain.RestWorkDays;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the RestWorkDays entity.
 */
@SuppressWarnings("unused")
public interface RestWorkDaysRepository extends JpaRepository<RestWorkDays,Long> {

}
