[![Build Status](https://travis-ci.org/thisarattr/grocery.svg?branch=master)](https://travis-ci.org/thisarattr/grocery)

# Grocery App

This sample application consists of two parts,
1. Springboot API (2.1.5) 
2. Angular 8 Web Client

All this allow to do is CRUD operation of the Items data on a grocery system. So this will allow us to create, edit, delete or search fetch items.

Little bit background on the application,

API Server
1. Security: Login api will check the database for user credentials and if its valid then generate a jwt with the configured jwt secret. Secret is configure in the property file but it can be read from environment to increase security. Then this jwt token will be returned back to user to be used in subsequent api calls.
Registration bean has used to forward all the secure api starting with `/api/v1/admin/*` into a `JwtFilter` which validate the token. If its valid user will get access to the api and else `401 unauthoried` exception is raised. 
2. There are two models `item` and `category` and there is a `many-to-many` relationship, allow item falls under multiple categories.
3. Google checkstyle is used
4. H2 is been used as the data storage
4. Integration test and dao test cover all the functionality and since there are minimum logic involved, less unit tests used. Overall test coverage is about 80-90%. Check the jacoco reports.

Angular Client
1. Pretty basic angular client with bit of bootstrap4 styles
2. Error handing is not good enough :(

## Libraries
Application is done basically using, spring boot framework, gradle as build tool and h2 as database.

API Server
* Java8
* Spring boot 2.1.5
* Hibernate 5.4.3.Final
* jjwt 0.7.0
* h2database
* httpclient 4.5.3

Client App
* Node 11.11.0
* npm 6.9.0
* Angular cli 8.0.0

## API Documentation

Please refer to `Grocery_API.postman_collection.json` postman collection export file for comprehensive api details.
https://www.getpostman.com/


## Build and run
Gradle is used as build tool and [travis](https://travis-ci.org/thisarattr/grocery) has been configured for continuous integration. 

##### API Server
This will compile, checkstyle and run all the tests.  
`./gradlew clean build`

Test and checkstyle reports can be found, `$projectDir/grocery/build/reports/`

This will package executable boot jar which can be run with below command.

`./gradlew bootJar`

Artifacts can be located at `$projectDir/build/lib/grocery-1.0.jar`, and below command will start the server at port 8080 and CORS exception added for post 4200 for angular client to be able to communicate with the server.

`java -jar $projectDir/build/lib/grocery-1.0.jar`

##### Web Client
This requires, above listed version or above for node, npm and angular cli

Below command will download all the required dependencies for the application to run and build the application.

`cd grocery-client`
`npm install`
`ng build --prod`

This will start the server on port `4200` and will communicate with api server which runs on `8080`.  
`ng serve`

##### User creds
Any of the below test users can be used to test the application/api
* user/password
* admin/password

