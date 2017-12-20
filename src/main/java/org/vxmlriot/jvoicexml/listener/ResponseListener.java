package org.vxmlriot.jvoicexml.listener;

import org.apache.log4j.Logger;
import org.jvoicexml.xml.ssml.SsmlDocument;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.apache.commons.collections4.CollectionUtils.isEmpty;

public class ResponseListener extends TextListenerAdapter {

    private static final Logger LOGGER = Logger.getLogger(ResponseListener.class);

    /**
     * Time to wait for first response.
     * If we exceed this time with no response, assume no response was sent.
     */
    private static final long TIME_TO_FIRST_RESPONSE_MS = 1000;

    /**
     * Time to wait between responses.
     * If we exceed this time after a response, assume all responses were sent.
     */
    private static final long TIME_TO_NEXT_RESPONSE_MS = 500;

    private boolean awaitingMoreResponses = true;
    private final List<SsmlDocument> capturedResponses = Collections.synchronizedList(new ArrayList<>());

    @Override
    public synchronized void outputSsml(SsmlDocument document) {
        capturedResponses.add(document);
        awaitingMoreResponses = true;
        notify();
    }

    /**
     * Return all text response output.
     * This method will block until all responses are received.
     * @return List of SsmlDocuments. A single VXML page with multiple
     *         speech sections will return multiple responses.
     */
    public List<SsmlDocument> getCapturedResponses() {

        // Wait for further responses
        while (awaitingMoreResponses) {
            awaitingMoreResponses = false;
            waitForNextResponse();
        }

        // Assume no more responses
        synchronized (capturedResponses) {
            // Return a copy of the list to prevent concurrent modification
            return new ArrayList<>(capturedResponses);
        }
    }

    private synchronized void waitForNextResponse() {
        long timeout = isEmpty(capturedResponses) ? TIME_TO_FIRST_RESPONSE_MS : TIME_TO_NEXT_RESPONSE_MS;
        try {
            wait(timeout);
        } catch (InterruptedException e) {
            LOGGER.warn("Interrupted while awaiting text response", e);
        }
    }
}