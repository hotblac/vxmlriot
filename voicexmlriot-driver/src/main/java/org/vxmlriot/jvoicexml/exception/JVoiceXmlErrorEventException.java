package org.vxmlriot.jvoicexml.exception;

import org.jvoicexml.event.ErrorEvent;

public class JVoiceXmlErrorEventException extends JVoiceXmlException {

    public JVoiceXmlErrorEventException(ErrorEvent event) {
        super("Error event: " + event.getEventType(), event);
    }
}
