package com.ctalpa.prxmen.repository.search;

import com.ctalpa.prxmen.domain.CompanyVehicles;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the CompanyVehicles entity.
 */
public interface CompanyVehiclesSearchRepository extends ElasticsearchRepository<CompanyVehicles, Long> {
}
