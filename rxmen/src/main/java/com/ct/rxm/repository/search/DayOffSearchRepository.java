package com.ct.rxm.repository.search;

import com.ct.rxm.domain.DayOff;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the DayOff entity.
 */
public interface DayOffSearchRepository extends ElasticsearchRepository<DayOff, Long> {
}
