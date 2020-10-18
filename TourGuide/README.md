# TourGuide
Welcome to TourGuide !

- TourGuide is part of the TripMaster company applications portfolio
- TourGuide is a web application which goal is to help users to travel
- The application is working but with some issues

The goal of this project is to : 
- enhance the performance of the application
- modify existing functionalities to meet users requests
- add a new functionality requested by product owners
- split and distribute the application 

For more information about the application, its functioning, the goals of this enhancement project and the results obtained, you can refer to the technical documentation attached in the repository.

### TourGuide Monolithic version

This version is a monolithic version of the TourGuide application. There is also a splitted and distributed version available for which you will find more information in the following repository (most up to date branch = *develop*):
<https://github.com/ob78/TourGuide_Distributed_MainApplication/tree/develop/TourGuide>

Technologies used are the following :
- Java is used as programming language
- SpringBoot is used for the web application which is based on the MVC pattern
- Server used is SpringBoot Tomcat embedded
- Gradle is used to run the tests, build and package the application
- JUnit is used as tests engine
- Mockito is used as mocking framework for tests

Services provided by the application are exposed using a REST APIs.

### Getting Started

The following instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

You need to install the following software :

- Java 8
- Gradle 4.8.1
- Docker
>You don't need to install SpringBoot by yourself because dependencies will be added by Gradle (Version of SpringBoot used is 2.1.6)

### Installing

You will find below a step by step explanation that tell you how to get a development environment running :

1.Install Java :
<https://docs.oracle.com/javase/8/docs/technotes/guides/install/install_overview.html>

2.Install Gradle :
<https://gradle.org/install/>

3.Install Docker :
<https://docs.docker.com/get-docker/>

### Application running

Then you can import and run the application from your favorite IDE.

>Please note that the application has been developed with the IntelliJ IDE.

### Configuration

There is a Spring configuration properties file called : *application.properties*.

In this file, you can manage the logging level and the sever listening port.

### Application initialization

When the TourGuide Application is launched, by default it is initialized with a certain number of users stored in memory (in a HashMap).
You can modify the number of initial users using the *internalUserNumber* in the *InternalTestHelper* class.

>You can disable this initialization by setting the *testMode* boolean in the *TourGuideService* class at false. 

### Endpoints

The following EndPoints are exposed by the application :

- GET  <http://localhost:8080/getLocation> : provide the location (being composed of latitude and longitude) of a user
>Request Parameter : *userName* = name of the user 

- GET  <http://localhost:8080/getNearbyAttractions> : provide the 5 closest attractions to the user location
>Request Parameter : *userName* = name of the user 

- GET  <http://localhost:8080/getRewards> : provide the rewards earned by the user
>Request Parameter : *userName* = name of the user 

- GET  <http://localhost:8080/getAllCurrentLocations> : provide the current locations for all users

- GET  <http://localhost:8080/getTripDeals> : provide the trip deals proposed by the travel agency network and depending on the rewards and preferences of the user
>Request Parameter : *userName* = name of the user 

- GET  <http://localhost:8080/getPreferences> : to get travel preferences of the user
>Request Parameter : *userName* = name of the user 

- POST <http://localhost:8080/postPreferences> : to post travel preferences of the user
>Request Parameter : *userName* = name of the user 
> / Request Body : *userPreferences* = preferences of the user

### Features branches

In addition to this most up to date *develop* branch, there are also the following branches available corresponding to each feature development :
- *feature_speedImprovement* : improvement of the application speed (track user location and calculate rewards functionalities)
- *feature_nearbyAttractions* : modifying an existing functionality to provide the 5 nearest attractions for a user
- *feature_allLocations* : adding a new functionality that provides the last location for all users
- *feature_userPreferences* : modifying an existing functionality to allow users to consult and modify their travel preferences as well as take into account of these preferences in the proposed trip deals

### Tests

Tests are included. You can run them using JUnit runner or using Gradle.

>For the performances tests, you can modify the number of users.
