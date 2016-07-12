package com.ctalpa.prxmen.repository;

import com.ctalpa.prxmen.domain.DayOff;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the DayOff entity.
 */
@SuppressWarnings("unused")
public interface DayOffRepository extends JpaRepository<DayOff,Long> {

}
