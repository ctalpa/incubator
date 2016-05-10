package com.ct.rxm.service;

import com.ct.rxm.domain.Job;
import com.ct.rxm.repository.JobRepository;
import com.ct.rxm.repository.search.JobSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Job.
 */
@Service
@Transactional
public class JobService {

    private final Logger log = LoggerFactory.getLogger(JobService.class);
    
    @Inject
    private JobRepository jobRepository;
    
    @Inject
    private JobSearchRepository jobSearchRepository;
    
    /**
     * Save a job.
     * 
     * @param job the entity to save
     * @return the persisted entity
     */
    public Job save(Job job) {
        log.debug("Request to save Job : {}", job);
        Job result = jobRepository.save(job);
        jobSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the jobs.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<Job> findAll() {
        log.debug("Request to get all Jobs");
        List<Job> result = jobRepository.findAll();
        return result;
    }

    /**
     *  Get one job by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Job findOne(Long id) {
        log.debug("Request to get Job : {}", id);
        Job job = jobRepository.findOne(id);
        return job;
    }

    /**
     *  Delete the  job by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Job : {}", id);
        jobRepository.delete(id);
        jobSearchRepository.delete(id);
    }

    /**
     * Search for the job corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Job> search(String query) {
        log.debug("Request to search Jobs for query {}", query);
        return StreamSupport
            .stream(jobSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
