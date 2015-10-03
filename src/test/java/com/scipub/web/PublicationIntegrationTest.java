package com.scipub.web;

import javax.inject.Inject;

import org.springframework.test.context.ContextConfiguration;

import com.scipub.service.PublicationService;

/**
 * End-to-end integration test
 * @author bozhanov
 *
 */
@ContextConfiguration
public class PublicationIntegrationTest {

    @Inject
    private PublicationService publicationService;

    public void paperSubmissionIntegrationTest() {
        // prepare
        // create user, forgetMe 
        
    }
}
