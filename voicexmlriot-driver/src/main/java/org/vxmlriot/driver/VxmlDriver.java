package org.vxmlriot.driver;

import org.vxmlriot.exception.CallIsActiveException;
import org.vxmlriot.exception.CallNotActiveException;
import org.vxmlriot.exception.DriverException;

import java.net.URI;
import java.util.List;

/**
 * Main interface to VoiceXMLRiot.
 * This simulates a VXML browser and user interactions. It allows user input
 * to be simulated and VXML responses to be verified.
 */
public interface VxmlDriver {

    /**
     * Get a VXML document by URL
     * @param resource identifying the VXML to be loaded.
     * @throws CallIsActiveException if the driver already has an active call
     * @throws DriverException on failure of underlying driver
     * @throws IllegalArgumentException on invalid resource
     */
    void get(String resource) throws DriverException;

    /**
     * Get a VXML document by URL
     * @param resource identifying the VXML to be loaded.
     * @throws CallIsActiveException if the driver already has an active call
     * @throws DriverException on failure of underlying driver
     * @throws IllegalArgumentException on invalid resource
     */
    void get(URI resource) throws DriverException;

    /**
     * Simulate user entering a string of DTMF digits
     * @param digits to be entered
     * @throws CallNotActiveException if no call is in progress
     * @throws DriverException on failure of underlying driver
     * @throws IllegalArgumentException if digits are not valid DTMF input
     *              (digits 0-9, # or *)
     */
    void enterDtmf(String digits) throws DriverException;

    /**
     * Simulate user saying a sequence of utterances
     * @param utterance spoken by user
     * @throws CallNotActiveException if no call is in progress
     * @throws DriverException on failure of underlying driver
     */
    void say(String... utterance) throws DriverException;

    /**
     * End the current call.
     * This is necessary only to end a call before the VXML flow completes.
     * If the VXML flow completes, it will end the call automatically.
     */
    void hangup();

    /**
     * Text response spoken by VXML document. Typically, a VXML browser would
     * render this via TTS.
     * @return list of text responses of current document
     * @throws CallNotActiveException if no call is in progress
     * @throws DriverException on failure of underlying driver
     */
    List<String> getTextResponse() throws DriverException;

    /**
     * Audio source files played by VXML document.
     * @return list of audio responses of current document
     * @throws CallNotActiveException if no call is in progress
     * @throws DriverException on failure of underlying driver
     */
    List<String> getAudioSrc() throws DriverException;

    /**
     * Cleanly shutdown the driver and stop / deallocate resources.
     */
    void shutdown();

}
