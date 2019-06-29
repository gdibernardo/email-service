package com.gdibernardo.emailservice.email.repository;

import com.gdibernardo.emailservice.email.repository.model.DatastoreEmail;
import com.google.appengine.api.datastore.*;
import org.springframework.stereotype.Repository;

import java.util.logging.Logger;

@Repository
public class DatastoreEmailRepository {

    private static final Logger log = Logger.getLogger(DatastoreEmailRepository.class.getName());

    private DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();

    public DatastoreEmail fetch(long emailId) throws EntityNotFoundException {
        log.info(String.format("DatastoreEmailRepository: fetching email with id %d", emailId));

        Entity fetchedEntity = datastoreService.get(DatastoreEmail.getKey(emailId));

        return DatastoreEmail.fromEntity(fetchedEntity);
    }

    public long persist(DatastoreEmail datastoreEmail) {
        log.info(String.format("DatastoreEmailRepository: persisting email %s", datastoreEmail.toString()));

        return datastoreService.put(datastoreEmail.getEntity()).getId();
    }
}
