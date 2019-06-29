package com.gdibernardo.emailservice.email;


//  Email Status Flow
//  ENQUEUED    ->  PENDING  ->  SENT
public enum EmailStatus {
    ENQUEUED,
    PENDING,
    SENT
    // BOUNCED  //  TO BE ADDED
}