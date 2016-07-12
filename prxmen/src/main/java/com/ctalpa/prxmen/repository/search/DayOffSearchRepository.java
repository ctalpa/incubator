package com.ctalpa.prxmen.repository.search;

import com.ctalpa.prxmen.domain.DayOff;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the DayOff entity.
 */
public interface DayOffSearchRepository extends ElasticsearchRepository<DayOff, Long> {
}
