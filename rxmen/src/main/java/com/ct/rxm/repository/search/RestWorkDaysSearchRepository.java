package com.ct.rxm.repository.search;

import com.ct.rxm.domain.RestWorkDays;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the RestWorkDays entity.
 */
public interface RestWorkDaysSearchRepository extends ElasticsearchRepository<RestWorkDays, Long> {
}
