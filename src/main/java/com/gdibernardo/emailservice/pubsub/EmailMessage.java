package com.gdibernardo.emailservice.pubsub;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;

public class EmailMessage {

    private long id;

    EmailMessage() {}

    public EmailMessage(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public static EmailMessage fromJSONString(String jsonString) throws Exception {
        return new ObjectMapper().readValue(jsonString, EmailMessage.class);
    }

    public PubsubMessage toPubSubMessage() throws JsonProcessingException {
        return PubsubMessage.newBuilder().setData(ByteString.copyFromUtf8(toJSON())).build();
    }

    private String toJSON() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(this);
    }
}
