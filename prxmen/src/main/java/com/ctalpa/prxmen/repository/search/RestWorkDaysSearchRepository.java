package com.ctalpa.prxmen.repository.search;

import com.ctalpa.prxmen.domain.RestWorkDays;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the RestWorkDays entity.
 */
public interface RestWorkDaysSearchRepository extends ElasticsearchRepository<RestWorkDays, Long> {
}
