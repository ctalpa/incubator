package com.ctalpa.prxmen.repository.search;

import com.ctalpa.prxmen.domain.Vendor;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Vendor entity.
 */
public interface VendorSearchRepository extends ElasticsearchRepository<Vendor, Long> {
}
