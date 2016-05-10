package com.ct.rxm.repository;

import com.ct.rxm.domain.DayOff;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the DayOff entity.
 */
public interface DayOffRepository extends JpaRepository<DayOff,Long> {

}
