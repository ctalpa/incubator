package com.ctalpa.prxmen.repository;

import com.ctalpa.prxmen.domain.CompanyVehicles;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the CompanyVehicles entity.
 */
@SuppressWarnings("unused")
public interface CompanyVehiclesRepository extends JpaRepository<CompanyVehicles,Long> {

}
