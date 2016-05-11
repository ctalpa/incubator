package com.ct.rxm.service;

import com.ct.rxm.domain.JobHistory;
import com.ct.rxm.repository.JobHistoryRepository;
import com.ct.rxm.repository.search.JobHistorySearchRepository;
import com.ct.rxm.web.rest.dto.JobHistoryDTO;
import com.ct.rxm.web.rest.mapper.JobHistoryMapper;
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
 * Service Implementation for managing JobHistory.
 */
@Service
@Transactional
public class JobHistoryService {

    private final Logger log = LoggerFactory.getLogger(JobHistoryService.class);
    
    @Inject
    private JobHistoryRepository jobHistoryRepository;
    
    @Inject
    private JobHistoryMapper jobHistoryMapper;
    
    @Inject
    private JobHistorySearchRepository jobHistorySearchRepository;
    
    /**
     * Save a jobHistory.
     * 
     * @param jobHistoryDTO the entity to save
     * @return the persisted entity
     */
    public JobHistoryDTO save(JobHistoryDTO jobHistoryDTO) {
        log.debug("Request to save JobHistory : {}", jobHistoryDTO);
        JobHistory jobHistory = jobHistoryMapper.jobHistoryDTOToJobHistory(jobHistoryDTO);
        jobHistory = jobHistoryRepository.save(jobHistory);
        JobHistoryDTO result = jobHistoryMapper.jobHistoryToJobHistoryDTO(jobHistory);
        jobHistorySearchRepository.save(jobHistory);
        return result;
    }

    /**
     *  Get all the jobHistories.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<JobHistoryDTO> findAll() {
        log.debug("Request to get all JobHistories");
        List<JobHistoryDTO> result = jobHistoryRepository.findAll().stream()
            .map(jobHistoryMapper::jobHistoryToJobHistoryDTO)
            .collect(Collectors.toCollection(LinkedList::new));
        return result;
    }

    /**
     *  Get one jobHistory by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public JobHistoryDTO findOne(Long id) {
        log.debug("Request to get JobHistory : {}", id);
        JobHistory jobHistory = jobHistoryRepository.findOne(id);
        JobHistoryDTO jobHistoryDTO = jobHistoryMapper.jobHistoryToJobHistoryDTO(jobHistory);
        return jobHistoryDTO;
    }

    /**
     *  Delete the  jobHistory by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete JobHistory : {}", id);
        jobHistoryRepository.delete(id);
        jobHistorySearchRepository.delete(id);
    }

    /**
     * Search for the jobHistory corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<JobHistoryDTO> search(String query) {
        log.debug("Request to search JobHistories for query {}", query);
        return StreamSupport
            .stream(jobHistorySearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(jobHistoryMapper::jobHistoryToJobHistoryDTO)
            .collect(Collectors.toList());
    }
}
