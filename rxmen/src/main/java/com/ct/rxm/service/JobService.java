package com.ct.rxm.service;

import com.ct.rxm.domain.Job;
import com.ct.rxm.repository.JobRepository;
import com.ct.rxm.repository.search.JobSearchRepository;
import com.ct.rxm.web.rest.dto.JobDTO;
import com.ct.rxm.web.rest.mapper.JobMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
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
    private JobMapper jobMapper;
    
    @Inject
    private JobSearchRepository jobSearchRepository;
    
    /**
     * Save a job.
     * 
     * @param jobDTO the entity to save
     * @return the persisted entity
     */
    public JobDTO save(JobDTO jobDTO) {
        log.debug("Request to save Job : {}", jobDTO);
        Job job = jobMapper.jobDTOToJob(jobDTO);
        job = jobRepository.save(job);
        JobDTO result = jobMapper.jobToJobDTO(job);
        jobSearchRepository.save(job);
        return result;
    }

    /**
     *  Get all the jobs.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<JobDTO> findAll() {
        log.debug("Request to get all Jobs");
        List<JobDTO> result = jobRepository.findAll().stream()
            .map(jobMapper::jobToJobDTO)
            .collect(Collectors.toCollection(LinkedList::new));
        return result;
    }

    /**
     *  Get one job by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public JobDTO findOne(Long id) {
        log.debug("Request to get Job : {}", id);
        Job job = jobRepository.findOne(id);
        JobDTO jobDTO = jobMapper.jobToJobDTO(job);
        return jobDTO;
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
    public List<JobDTO> search(String query) {
        log.debug("Request to search Jobs for query {}", query);
        return StreamSupport
            .stream(jobSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(jobMapper::jobToJobDTO)
            .collect(Collectors.toList());
    }
}
