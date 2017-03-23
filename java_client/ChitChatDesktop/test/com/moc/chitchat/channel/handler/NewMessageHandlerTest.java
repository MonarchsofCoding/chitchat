package com.moc.chitchat.channel.handler;

import com.moc.chitchat.controller.MessageController;
import com.moc.chitchat.exception.UnexpectedResponseException;
import org.json.JSONObject;
import org.junit.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

/**
 * NewMessageHandlerTest provides tests for the NewMessageHandler
 */
public class NewMessageHandlerTest {

    @Test
    public void testConstructor() {
        MessageController messageController = mock(MessageController.class);
        NewMessageHandler newMessageHandler = new NewMessageHandler(messageController);

        assertNotNull(newMessageHandler);
        assertEquals(NewMessageHandler.class, newMessageHandler.getClass());
    }

    @Test
    public void testGetEvent() {
        MessageController messageController = mock(MessageController.class);
        NewMessageHandler newMessageHandler = new NewMessageHandler(messageController);

        String event = newMessageHandler.getEvent();

        assertEquals("new:message", event);
    }

    @Test
    public void testHandleMessage() {
        MessageController messageController = mock(MessageController.class);
        NewMessageHandler newMessageHandler = new NewMessageHandler(messageController);

        String text = "{\"from\":\"bob\",\"body\":\"WVX2VLMf/LKh4DYRrHm9aV8s3rLn1jB63l2OcpW8gVSJNiFvrw3E1GOQPn3Y6z4xO9b1g0QHatMBoLujmiCBcTAzvWyngL2/+2K+YqJJt6WDKjUgGgXPJJcVdHrvD47U83XWqmEOh70IGmHteAfGHxAv0lQekylBZ8oLtFh3lk0BLf5djlu/UCs2oc3037QAb/uMXiqtwDcpvNQCVDYTgLJJKvB1ru7ag8vb4ta4+cENQEvfH37GvQIvoVTtqSyHkOY9z1XbEi3RLIgxdPECJdaV+kP4Q1iQSg17oO/fZSziNSQMrjY34zHq7092hzVQfupzu9Mu7q3de+W/+2iRQLggPa5SPiqx9xIMcjtbJwx5yfGoVfYYtobIXN05DOuSsUVopLCBk07o3Cgm7SlGIbyB2R9ZieA8CtNcKnL9UsMrX3lWL5DfFae0081hEyTbMasCy9zVPTDbxojVkf+YQC6UwsgdAsnVpl8G8ze5Z3uSkHMI+QFMiaLOXrSK/wRLpFIbuuTqDxKkzSC0pR9UT5VAQ0rQaPHAb1W4iPYtkIJ7VOXzVD36m8L6ZlZU/PM8MVoTbcZ2lB5jwp9pcph8UD7GuVIHkQ/UV9McwvaKvXrB8bIlACwT+Q4WIFCCTAT3Fia8EpvC91ZgYUALaGQWSOQ+A9JMzZEXHxQoMBwBsew=\"}";
        JSONObject payload = new JSONObject(text);

        newMessageHandler.handleMessage(payload);
    }

    @Test
    public void testHandleMessageCatchException() throws BadPaddingException, InvalidKeySpecException, NoSuchAlgorithmException, IllegalBlockSizeException, UnexpectedResponseException, NoSuchPaddingException, InvalidKeyException, IOException {
        MessageController messageController = mock(MessageController.class);
        NewMessageHandler newMessageHandler = new NewMessageHandler(messageController);

        String text = "{\"from\": \"bob\", \"body\" : \"\"}";
        JSONObject payload = new JSONObject(text);

        String from = payload.getString("from");
        String body = payload.getString("body");

        when(messageController.receive(body, from)).thenThrow(new BadPaddingException());

        newMessageHandler.handleMessage(payload);
    }


}
