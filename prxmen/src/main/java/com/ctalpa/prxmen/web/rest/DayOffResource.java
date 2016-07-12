package com.ctalpa.prxmen.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ctalpa.prxmen.domain.DayOff;
import com.ctalpa.prxmen.repository.DayOffRepository;
import com.ctalpa.prxmen.repository.search.DayOffSearchRepository;
import com.ctalpa.prxmen.web.rest.util.HeaderUtil;
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
 * REST controller for managing DayOff.
 */
@RestController
@RequestMapping("/api")
public class DayOffResource {

    private final Logger log = LoggerFactory.getLogger(DayOffResource.class);
        
    @Inject
    private DayOffRepository dayOffRepository;
    
    @Inject
    private DayOffSearchRepository dayOffSearchRepository;
    
    /**
     * POST  /day-offs : Create a new dayOff.
     *
     * @param dayOff the dayOff to create
     * @return the ResponseEntity with status 201 (Created) and with body the new dayOff, or with status 400 (Bad Request) if the dayOff has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/day-offs",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DayOff> createDayOff(@RequestBody DayOff dayOff) throws URISyntaxException {
        log.debug("REST request to save DayOff : {}", dayOff);
        if (dayOff.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("dayOff", "idexists", "A new dayOff cannot already have an ID")).body(null);
        }
        DayOff result = dayOffRepository.save(dayOff);
        dayOffSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/day-offs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("dayOff", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /day-offs : Updates an existing dayOff.
     *
     * @param dayOff the dayOff to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated dayOff,
     * or with status 400 (Bad Request) if the dayOff is not valid,
     * or with status 500 (Internal Server Error) if the dayOff couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/day-offs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DayOff> updateDayOff(@RequestBody DayOff dayOff) throws URISyntaxException {
        log.debug("REST request to update DayOff : {}", dayOff);
        if (dayOff.getId() == null) {
            return createDayOff(dayOff);
        }
        DayOff result = dayOffRepository.save(dayOff);
        dayOffSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("dayOff", dayOff.getId().toString()))
            .body(result);
    }

    /**
     * GET  /day-offs : get all the dayOffs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of dayOffs in body
     */
    @RequestMapping(value = "/day-offs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<DayOff> getAllDayOffs() {
        log.debug("REST request to get all DayOffs");
        List<DayOff> dayOffs = dayOffRepository.findAll();
        return dayOffs;
    }

    /**
     * GET  /day-offs/:id : get the "id" dayOff.
     *
     * @param id the id of the dayOff to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the dayOff, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/day-offs/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DayOff> getDayOff(@PathVariable Long id) {
        log.debug("REST request to get DayOff : {}", id);
        DayOff dayOff = dayOffRepository.findOne(id);
        return Optional.ofNullable(dayOff)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /day-offs/:id : delete the "id" dayOff.
     *
     * @param id the id of the dayOff to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/day-offs/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteDayOff(@PathVariable Long id) {
        log.debug("REST request to delete DayOff : {}", id);
        dayOffRepository.delete(id);
        dayOffSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("dayOff", id.toString())).build();
    }

    /**
     * SEARCH  /_search/day-offs?query=:query : search for the dayOff corresponding
     * to the query.
     *
     * @param query the query of the dayOff search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/day-offs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<DayOff> searchDayOffs(@RequestParam String query) {
        log.debug("REST request to search DayOffs for query {}", query);
        return StreamSupport
            .stream(dayOffSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
