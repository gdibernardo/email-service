package com.gdibernardo.emailservice.email.repository.model;

import com.gdibernardo.emailservice.email.Email;
import com.gdibernardo.emailservice.email.EmailAddress;
import com.gdibernardo.emailservice.email.EmailStatus;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import java.util.Date;

public class DatastoreEmail {

    private static final String DATASTORE_EMAIL_TYPE = "EMAIL_TYPE";

    /*  Datastore property names.   */
    private static final String FROM_NAME = "fromName";
    private static final String FROM_EMAIL = "fromEmail";
    private static final String TO_NAME = "toName";
    private static final String TO_EMAIL = "toEmail";
    private static final String SUBJECT = "subject";
    private static final String CONTENT = "content";
    private static final String STATUS = "status";
    private static final String MODIFIED_AT = "modifiedAt";

    private Email email;

    private Date modifiedAt;

    public DatastoreEmail(Email email) {
        this.email = email;

        this.modifiedAt = new Date();
    }

    public Email getEmail() {
        return email;
    }

    public Date getModifiedAt() {
        return modifiedAt;
    }

    public Entity getEntity() {
        if(email.getId() == 0) {
            return buildEntity(new Entity(DATASTORE_EMAIL_TYPE));
        }

        return buildEntity(new Entity(getKey(email.getId())));
    }

    public static Key getKey(long emailMessageId) {
        return KeyFactory.createKey(DATASTORE_EMAIL_TYPE, emailMessageId);
    }

    public static DatastoreEmail fromEntity(Entity entity) {

        DatastoreEmail datastoreEmail = new DatastoreEmail(new Email(
                entity.getKey().getId(),
                new EmailAddress((String) entity.getProperty(FROM_EMAIL), (String) entity.getProperty(FROM_NAME)),
                new EmailAddress((String) entity.getProperty(TO_EMAIL), (String) entity.getProperty(TO_NAME)),
                (String) entity.getProperty(SUBJECT),
                (String) entity.getProperty(CONTENT),
                EmailStatus.valueOf((String) entity.getProperty(STATUS))));

        datastoreEmail.modifiedAt = ((Date) entity.getProperty(MODIFIED_AT));

        return datastoreEmail;
    }

    private Entity buildEntity(Entity entity) {
        entity.setProperty(FROM_NAME, email.getFrom().getName());
        entity.setProperty(FROM_EMAIL, email.getFrom().getAddress());
        entity.setProperty(TO_NAME, email.getTo().getName());
        entity.setProperty(TO_EMAIL, email.getTo().getAddress());
        entity.setProperty(SUBJECT, email.getSubject());
        entity.setProperty(CONTENT, email.getContent());
        entity.setProperty(STATUS, email.getStatus().name());
        entity.setUnindexedProperty(MODIFIED_AT, modifiedAt);

        return entity;
    }
}
