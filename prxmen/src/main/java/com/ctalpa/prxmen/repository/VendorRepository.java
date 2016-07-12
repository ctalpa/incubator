package com.ctalpa.prxmen.repository;

import com.ctalpa.prxmen.domain.Vendor;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Vendor entity.
 */
@SuppressWarnings("unused")
public interface VendorRepository extends JpaRepository<Vendor,Long> {

}
