# SOEN 6441 - Warzone Game

Here, Warzone game is implemented,where all players try to win the territories by attacking with the armies they have.

With the representation of unique maps that they will have and get options to choose or update the maps,this game is quite attractive with the GUI implementation as well as with Command line interface(In GUI).

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

Java Version 11

```
java -version
```

### Installing 

A step by step series of commands that tell you how to get a development env running

Go to Project Directory : 

```
cd warzone 
```
Build With Maven :

```
mvn clean install -DskipTests
```

## Running the Project (After Installation)

Go to Project Directory :

```
cd warzone 
```

Run the test :

```
java -jar ./target/warzone-0.0.1-SNAPSHOT.jar
```

## Running the tests

Go to Project Directory :

```
cd warzone 
```

Run the test :

```
mvn -Dtest=WarzoneApplicationTests test
```

