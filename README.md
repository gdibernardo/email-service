# Email Service
For the Uber coding challenge (https://github.com/uber-archive/coding-challenge-tools/blob/master/coding_challenge.md) I implemented the Email Service app. My solution focuses on the backend. 

The Email Service is powered by the following email providers:

- **Sendgrid** (https://www.sendgrid.com)

- **Mailjet** (https://www.mailjet.com)

The application is able to detect if one of the email providers is not available and failover to the other one.

**Link to the app:** https://email-service-241013.appspot.com

**DO NOTE:** The submitted email message might end up in your spam folder! 


## Run locally

In order to run the application on your local environment, you need to: 
 
- Install the App Engine component.
- Enable Pub/Sub and create a valid topic and subscription. The subscription has to be of **push** type; the push endpoint is: /pubsub/push?token=**YOUR-SECRET-TOKEN**.
- Create an account on Sendgrid and Mailjet.

Furthermore, you need to setup the app configuration in the following files:
- src/main/webapp/WEB-INF/appengine-web.xml 
- src/main/resources/application.yml 

You can run the application locally with the following command: 
```
mvn appengine:run
```


# Design & Implementation

I have implemented the Email Service application using Java and the Spring Boot Framework for the backend; the app also has a simple web UI implemented using React.

I have chosen to use Spring Boot because with this framework it is very easy to setup a web-application that can grow very fast in complexity among different contributors.

I have some working experience with Spring, while I have never used React before. 

The application is deployed on Google Cloud Platform using App Engine. I chose this cloud tool because it allows easily deployment of a Java application without worrying about the underneath infrastructure. However, the application can be easily containerized and deployed everywhere else.
The application also relies on Google Pub/Sub message service. I chose to use this tool since my app is deployed on GCP and for avoiding the burden of setup the infrastructure for a message broker. Furthermore, Pub/Sub can autoscale and handle any load of traffic. However, others message broker implementations available out there might be a good fit for this type of application. In case we want to decouple our app from Google Cloud Platform, we can easily change the message broker client used by the application.



### Architecture

From a high-level perspective the application is structured as follow:

- **The web UI frontend** This layer offers a (very!!!!) basic UI for sending emails.

- **The backend REST layer** This layer offers a simple and generic REST POST endpoint for sending email messages. This layer is also responsible for publishing (in a synchronous fashion) the email messages to the message queue. 
- **The backend email sender layer**  This layer consumes email messages from the Pub/Sub message queue (via push mechanism, this is required because of the App Engine nature). Then, it submits the email using one of the available email providers. If there is no email provider available for sending the email, the message is kept in the queue: Pub/Sub will retry to deliver the message again after 60 seconds. In this way, no messages are lost on their way to the recipient if both the email providers are not available or if the entire layer is down.
- **The Pub/Sub message queue** That acts as the backbone of the application and its components.

For sake of simplicity, the application comes as a single deployable unit (and code base), but the above components might be deployed separately (the frontend should **definitely** be deployed **separately** and probably lives in a completely different project). 

This architecture can scale horizontally very well: we might want to scale-out one or more of the listed components, for example, in order to handle a massive load of requests.

### REST API
The application offers just a simple endpoint:
```
POST /emails/submit
```
#### Data params

```
{
    "to": {"address":[string], "name":[string]},
    "from": {"address":[string], "name":[string]},
    "subject":[string],
    "content":[string]
}
```

The `name` field is optional in both the `to` and `from` parameter. The `address` field in `from` object can be omitted. **DO NOTE**: Because Mailjet requires to validate the sender email address, the application will use a system sender email address when sending an email. However, the user can still specify its name.

The `subject` and `content` fields are mandatory for this version of the API.



#### Example

```
curl -i -H "Accept: application/json" -H "Content-Type:application/json" -X POST --data '{"to":{"address":"RECIPIENT_EMAIL@ADDRESS.COM"}, "from": {"address": "SENDER_EMAIL@ADDRESS.IO", "name":"SENDER NAME"}, "content":"Scaramouch, Scaramouch will you do the Fandango, Thunderbolt and lightning very very frightening me,\n Gallileo, Gallileo, Gallileo, Gallileo, Gallileo, figaro, magnifico", "subject":"I see a little silhouetto of a man"}' "https://email-service-241013.appspot.com/emails/submit"
```

#### Response

```
{"message":"Email correctly enqueued.","status":200}
```

## TO-DOs:
- Pub/Sub is an **At-Least-Once delivery** guarantee system: that means that potentially an email message might be sent more than once. Since we do not want that our users have an unpleasant experience, we can deduplicate messages at the application layer. This can be achieved by uniquely identifying each message (the application already assigns a unique id to each email message) and introducing a storage layer. With such an addition, the application can also be enriched with even more functionality to the end user such as an endpoint for querying/polling the current status of a certain email (e.g., enqueued, pending, sent, etc.) or a UI page for displaying the sent emails. 
- As already mentioned, the UI web frontend should live in a different module and be deployed separately.
- Improve testing; run tests against Pub/Sub emulator; add system tests; thoroughly test the endpoint(s).
- Add support for HTML content and email attachments. 
- Add support for multiple recipients.
- Add support for CC.
- Improve and test UI.

## About Me

**LinkedIn:** https://www.linkedin.com/in/gabrieledibernardo

**Github:** https://github.com/gdibernardo