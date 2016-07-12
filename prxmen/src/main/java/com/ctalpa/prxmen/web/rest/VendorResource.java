package com.ctalpa.prxmen.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ctalpa.prxmen.domain.Vendor;
import com.ctalpa.prxmen.repository.VendorRepository;
import com.ctalpa.prxmen.repository.search.VendorSearchRepository;
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
 * REST controller for managing Vendor.
 */
@RestController
@RequestMapping("/api")
public class VendorResource {

    private final Logger log = LoggerFactory.getLogger(VendorResource.class);
        
    @Inject
    private VendorRepository vendorRepository;
    
    @Inject
    private VendorSearchRepository vendorSearchRepository;
    
    /**
     * POST  /vendors : Create a new vendor.
     *
     * @param vendor the vendor to create
     * @return the ResponseEntity with status 201 (Created) and with body the new vendor, or with status 400 (Bad Request) if the vendor has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/vendors",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Vendor> createVendor(@RequestBody Vendor vendor) throws URISyntaxException {
        log.debug("REST request to save Vendor : {}", vendor);
        if (vendor.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("vendor", "idexists", "A new vendor cannot already have an ID")).body(null);
        }
        Vendor result = vendorRepository.save(vendor);
        vendorSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/vendors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("vendor", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /vendors : Updates an existing vendor.
     *
     * @param vendor the vendor to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated vendor,
     * or with status 400 (Bad Request) if the vendor is not valid,
     * or with status 500 (Internal Server Error) if the vendor couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/vendors",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Vendor> updateVendor(@RequestBody Vendor vendor) throws URISyntaxException {
        log.debug("REST request to update Vendor : {}", vendor);
        if (vendor.getId() == null) {
            return createVendor(vendor);
        }
        Vendor result = vendorRepository.save(vendor);
        vendorSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("vendor", vendor.getId().toString()))
            .body(result);
    }

    /**
     * GET  /vendors : get all the vendors.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of vendors in body
     */
    @RequestMapping(value = "/vendors",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Vendor> getAllVendors() {
        log.debug("REST request to get all Vendors");
        List<Vendor> vendors = vendorRepository.findAll();
        return vendors;
    }

    /**
     * GET  /vendors/:id : get the "id" vendor.
     *
     * @param id the id of the vendor to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the vendor, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/vendors/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Vendor> getVendor(@PathVariable Long id) {
        log.debug("REST request to get Vendor : {}", id);
        Vendor vendor = vendorRepository.findOne(id);
        return Optional.ofNullable(vendor)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /vendors/:id : delete the "id" vendor.
     *
     * @param id the id of the vendor to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/vendors/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteVendor(@PathVariable Long id) {
        log.debug("REST request to delete Vendor : {}", id);
        vendorRepository.delete(id);
        vendorSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("vendor", id.toString())).build();
    }

    /**
     * SEARCH  /_search/vendors?query=:query : search for the vendor corresponding
     * to the query.
     *
     * @param query the query of the vendor search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/vendors",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Vendor> searchVendors(@RequestParam String query) {
        log.debug("REST request to search Vendors for query {}", query);
        return StreamSupport
            .stream(vendorSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
