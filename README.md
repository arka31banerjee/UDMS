# UserDataManagementSystem [UDMS]
Hypothetical business use case : FOODCOMCAB is a multisector business organization and thus, running multiple applications. Now, They need a central repository to store user/subscribers info/contact and a file related to that user.
Applications owned by FOODCOMCAB given below :
GODELIVER : Food delivery application
FAREMARKET : Product selling E-commerce application
FOODWHEELS : Cab service application

Basically, It's a Spring Boot web application to perform CRUD operation on a user.
Tech Used: Java 17, Spring Boot 3.1.0, DB : mongoDB
All Apis can be tested through Postman. Also, for documentation part, swagger is enabled.

For better visibility and high level design sharing a User info in JSON format [Comment given inline] :

{
  "_id": {
    "$oid": "649049db2493795f27bc688d" //**MongoDB generated primary key**
  },
  "userId": {
    "$numberLong": "1" // **Sequence generated userId**
  },
  "firstName": "Jack", // **Bean validation applied --> non empty**
  "lastName": "Smith",  // **Bean validation applied --> non empty**
  "email": "yourjs561@gmail.com", // **Bean validation applied --> non empty +  Should belong to GODELIVER app only once [may belong to other application]**
  "application": "GODELIVER", // **validation applied through interceptor --> GODELIVER/FAREMARKET/FOODWHEELS (this are @PathVariable)**
  "createdDate": {
    "$date": "2023-06-19T12:28:11.057Z" // **Before converting User entity to BSON mongo entity this will be autogenrated through MongoEventListener**
  },
  "lastUpdated": {
    "$date": "2023-06-19T12:28:11.057Z" // **same as created date**
  },
  "fileReference": "C:\\Temp\\a6b9c-My Certificate.pdf", // **Typically it should get stored in some secure vault/server, Stored in local due to limitation**
  // **This is downlodable**
  "infoTags": [
    {
      "infoKey": "contact",
      "infoValue": "214520123"
    },
    {
      "infoKey": "age",
      "infoValue": 27
    }
  ],
  // **infoTags is all about storing aditional information related to user,dynamically. Custom validation applied on dataType of each, can be enhanced.Putting key value pair,other than age,occupation,contact,likings wont cause any issue, as well as wont get stored in DB. AppId wise these set of keys can be enhanced.**
}
