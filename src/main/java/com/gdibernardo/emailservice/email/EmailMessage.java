package com.gdibernardo.emailservice.email;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.gdibernardo.emailservice.email.api.Email;

import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;

import java.util.UUID;

public class EmailMessage {
    private String id;

    private String from;
    private String to;
    private String subject;
    private String content;

    private EmailMessage() {}

    public EmailMessage(String id, String from, String to, String subject, String content) {
        this.id = id;

        this.from = from;
        this.to = to;
        this.subject = subject;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public String getSubject() {
        return subject;
    }

    public String getContent() {
        return content;
    }

    public PubsubMessage toPubSubMessage() throws JsonProcessingException {
        return PubsubMessage.newBuilder().setData(ByteString.copyFromUtf8(toJSON())).build();
    }

    @Override
    public String toString() {
        return "EmailMessage{" +
                "id='" + id + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", subject='" + subject + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    public static EmailMessage fromEmail(Email email) {
        return new EmailMessage(UUID.randomUUID().toString(),
                email.getFrom(),
                email.getTo(),
                email.getSubject(),
                email.getContent());
    }

    public static EmailMessage fromJSONString(String jsonString) throws Exception {
        return new ObjectMapper().readValue(jsonString, EmailMessage.class);
    }

    private String toJSON() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(this);
    }
}
