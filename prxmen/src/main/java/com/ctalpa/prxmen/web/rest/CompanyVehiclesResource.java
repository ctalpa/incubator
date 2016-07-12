package com.ctalpa.prxmen.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ctalpa.prxmen.domain.CompanyVehicles;
import com.ctalpa.prxmen.repository.CompanyVehiclesRepository;
import com.ctalpa.prxmen.repository.search.CompanyVehiclesSearchRepository;
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
 * REST controller for managing CompanyVehicles.
 */
@RestController
@RequestMapping("/api")
public class CompanyVehiclesResource {

    private final Logger log = LoggerFactory.getLogger(CompanyVehiclesResource.class);
        
    @Inject
    private CompanyVehiclesRepository companyVehiclesRepository;
    
    @Inject
    private CompanyVehiclesSearchRepository companyVehiclesSearchRepository;
    
    /**
     * POST  /company-vehicles : Create a new companyVehicles.
     *
     * @param companyVehicles the companyVehicles to create
     * @return the ResponseEntity with status 201 (Created) and with body the new companyVehicles, or with status 400 (Bad Request) if the companyVehicles has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/company-vehicles",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CompanyVehicles> createCompanyVehicles(@RequestBody CompanyVehicles companyVehicles) throws URISyntaxException {
        log.debug("REST request to save CompanyVehicles : {}", companyVehicles);
        if (companyVehicles.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("companyVehicles", "idexists", "A new companyVehicles cannot already have an ID")).body(null);
        }
        CompanyVehicles result = companyVehiclesRepository.save(companyVehicles);
        companyVehiclesSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/company-vehicles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("companyVehicles", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /company-vehicles : Updates an existing companyVehicles.
     *
     * @param companyVehicles the companyVehicles to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated companyVehicles,
     * or with status 400 (Bad Request) if the companyVehicles is not valid,
     * or with status 500 (Internal Server Error) if the companyVehicles couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/company-vehicles",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CompanyVehicles> updateCompanyVehicles(@RequestBody CompanyVehicles companyVehicles) throws URISyntaxException {
        log.debug("REST request to update CompanyVehicles : {}", companyVehicles);
        if (companyVehicles.getId() == null) {
            return createCompanyVehicles(companyVehicles);
        }
        CompanyVehicles result = companyVehiclesRepository.save(companyVehicles);
        companyVehiclesSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("companyVehicles", companyVehicles.getId().toString()))
            .body(result);
    }

    /**
     * GET  /company-vehicles : get all the companyVehicles.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of companyVehicles in body
     */
    @RequestMapping(value = "/company-vehicles",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<CompanyVehicles> getAllCompanyVehicles() {
        log.debug("REST request to get all CompanyVehicles");
        List<CompanyVehicles> companyVehicles = companyVehiclesRepository.findAll();
        return companyVehicles;
    }

    /**
     * GET  /company-vehicles/:id : get the "id" companyVehicles.
     *
     * @param id the id of the companyVehicles to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the companyVehicles, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/company-vehicles/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CompanyVehicles> getCompanyVehicles(@PathVariable Long id) {
        log.debug("REST request to get CompanyVehicles : {}", id);
        CompanyVehicles companyVehicles = companyVehiclesRepository.findOne(id);
        return Optional.ofNullable(companyVehicles)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /company-vehicles/:id : delete the "id" companyVehicles.
     *
     * @param id the id of the companyVehicles to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/company-vehicles/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCompanyVehicles(@PathVariable Long id) {
        log.debug("REST request to delete CompanyVehicles : {}", id);
        companyVehiclesRepository.delete(id);
        companyVehiclesSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("companyVehicles", id.toString())).build();
    }

    /**
     * SEARCH  /_search/company-vehicles?query=:query : search for the companyVehicles corresponding
     * to the query.
     *
     * @param query the query of the companyVehicles search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/company-vehicles",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<CompanyVehicles> searchCompanyVehicles(@RequestParam String query) {
        log.debug("REST request to search CompanyVehicles for query {}", query);
        return StreamSupport
            .stream(companyVehiclesSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
