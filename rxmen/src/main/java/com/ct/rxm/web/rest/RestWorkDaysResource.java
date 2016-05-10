package com.ct.rxm.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ct.rxm.domain.RestWorkDays;
import com.ct.rxm.repository.RestWorkDaysRepository;
import com.ct.rxm.repository.search.RestWorkDaysSearchRepository;
import com.ct.rxm.web.rest.util.HeaderUtil;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing RestWorkDays.
 */
@RestController
@RequestMapping("/api")
public class RestWorkDaysResource {

    private final Logger log = LoggerFactory.getLogger(RestWorkDaysResource.class);
        
    @Inject
    private RestWorkDaysRepository restWorkDaysRepository;
    
    @Inject
    private RestWorkDaysSearchRepository restWorkDaysSearchRepository;
    
    /**
     * POST  /rest-work-days : Create a new restWorkDays.
     *
     * @param restWorkDays the restWorkDays to create
     * @return the ResponseEntity with status 201 (Created) and with body the new restWorkDays, or with status 400 (Bad Request) if the restWorkDays has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/rest-work-days",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RestWorkDays> createRestWorkDays(@RequestBody RestWorkDays restWorkDays) throws URISyntaxException {
        log.debug("REST request to save RestWorkDays : {}", restWorkDays);
        if (restWorkDays.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("restWorkDays", "idexists", "A new restWorkDays cannot already have an ID")).body(null);
        }
        RestWorkDays result = restWorkDaysRepository.save(restWorkDays);
        restWorkDaysSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/rest-work-days/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("restWorkDays", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /rest-work-days : Updates an existing restWorkDays.
     *
     * @param restWorkDays the restWorkDays to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated restWorkDays,
     * or with status 400 (Bad Request) if the restWorkDays is not valid,
     * or with status 500 (Internal Server Error) if the restWorkDays couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/rest-work-days",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RestWorkDays> updateRestWorkDays(@RequestBody RestWorkDays restWorkDays) throws URISyntaxException {
        log.debug("REST request to update RestWorkDays : {}", restWorkDays);
        if (restWorkDays.getId() == null) {
            return createRestWorkDays(restWorkDays);
        }
        RestWorkDays result = restWorkDaysRepository.save(restWorkDays);
        restWorkDaysSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("restWorkDays", restWorkDays.getId().toString()))
            .body(result);
    }

    /**
     * GET  /rest-work-days : get all the restWorkDays.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of restWorkDays in body
     */
    @RequestMapping(value = "/rest-work-days",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<RestWorkDays> getAllRestWorkDays() {
        log.debug("REST request to get all RestWorkDays");
        List<RestWorkDays> restWorkDays = restWorkDaysRepository.findAll();
        return restWorkDays;
    }

    /**
     * GET  /rest-work-days/:id : get the "id" restWorkDays.
     *
     * @param id the id of the restWorkDays to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the restWorkDays, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/rest-work-days/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RestWorkDays> getRestWorkDays(@PathVariable Long id) {
        log.debug("REST request to get RestWorkDays : {}", id);
        RestWorkDays restWorkDays = restWorkDaysRepository.findOne(id);
        return Optional.ofNullable(restWorkDays)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /rest-work-days/:id : delete the "id" restWorkDays.
     *
     * @param id the id of the restWorkDays to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/rest-work-days/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteRestWorkDays(@PathVariable Long id) {
        log.debug("REST request to delete RestWorkDays : {}", id);
        restWorkDaysRepository.delete(id);
        restWorkDaysSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("restWorkDays", id.toString())).build();
    }

    /**
     * SEARCH  /_search/rest-work-days?query=:query : search for the restWorkDays corresponding
     * to the query.
     *
     * @param query the query of the restWorkDays search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/rest-work-days",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<RestWorkDays> searchRestWorkDays(@RequestParam String query) {
        log.debug("REST request to search RestWorkDays for query {}", query);
        return StreamSupport
            .stream(restWorkDaysSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
