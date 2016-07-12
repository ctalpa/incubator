package com.ctalpa.prxmen.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ctalpa.prxmen.domain.JobHistory;
import com.ctalpa.prxmen.service.JobHistoryService;
import com.ctalpa.prxmen.web.rest.util.HeaderUtil;
import com.ctalpa.prxmen.web.rest.dto.JobHistoryDTO;
import com.ctalpa.prxmen.web.rest.mapper.JobHistoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing JobHistory.
 */
@RestController
@RequestMapping("/api")
public class JobHistoryResource {

    private final Logger log = LoggerFactory.getLogger(JobHistoryResource.class);
        
    @Inject
    private JobHistoryService jobHistoryService;
    
    @Inject
    private JobHistoryMapper jobHistoryMapper;
    
    /**
     * POST  /job-histories : Create a new jobHistory.
     *
     * @param jobHistoryDTO the jobHistoryDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new jobHistoryDTO, or with status 400 (Bad Request) if the jobHistory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/job-histories",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<JobHistoryDTO> createJobHistory(@RequestBody JobHistoryDTO jobHistoryDTO) throws URISyntaxException {
        log.debug("REST request to save JobHistory : {}", jobHistoryDTO);
        if (jobHistoryDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("jobHistory", "idexists", "A new jobHistory cannot already have an ID")).body(null);
        }
        JobHistoryDTO result = jobHistoryService.save(jobHistoryDTO);
        return ResponseEntity.created(new URI("/api/job-histories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("jobHistory", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /job-histories : Updates an existing jobHistory.
     *
     * @param jobHistoryDTO the jobHistoryDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated jobHistoryDTO,
     * or with status 400 (Bad Request) if the jobHistoryDTO is not valid,
     * or with status 500 (Internal Server Error) if the jobHistoryDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/job-histories",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<JobHistoryDTO> updateJobHistory(@RequestBody JobHistoryDTO jobHistoryDTO) throws URISyntaxException {
        log.debug("REST request to update JobHistory : {}", jobHistoryDTO);
        if (jobHistoryDTO.getId() == null) {
            return createJobHistory(jobHistoryDTO);
        }
        JobHistoryDTO result = jobHistoryService.save(jobHistoryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("jobHistory", jobHistoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /job-histories : get all the jobHistories.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of jobHistories in body
     */
    @RequestMapping(value = "/job-histories",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<JobHistoryDTO> getAllJobHistories() {
        log.debug("REST request to get all JobHistories");
        return jobHistoryService.findAll();
    }

    /**
     * GET  /job-histories/:id : get the "id" jobHistory.
     *
     * @param id the id of the jobHistoryDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the jobHistoryDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/job-histories/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<JobHistoryDTO> getJobHistory(@PathVariable Long id) {
        log.debug("REST request to get JobHistory : {}", id);
        JobHistoryDTO jobHistoryDTO = jobHistoryService.findOne(id);
        return Optional.ofNullable(jobHistoryDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /job-histories/:id : delete the "id" jobHistory.
     *
     * @param id the id of the jobHistoryDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/job-histories/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteJobHistory(@PathVariable Long id) {
        log.debug("REST request to delete JobHistory : {}", id);
        jobHistoryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("jobHistory", id.toString())).build();
    }

    /**
     * SEARCH  /_search/job-histories?query=:query : search for the jobHistory corresponding
     * to the query.
     *
     * @param query the query of the jobHistory search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/job-histories",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<JobHistoryDTO> searchJobHistories(@RequestParam String query) {
        log.debug("REST request to search JobHistories for query {}", query);
        return jobHistoryService.search(query);
    }


}
