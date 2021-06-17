# Usage info
## Build
To build an executable jar file run:
```
mvn clean install
```
## Run
A sample command to run the application:
```
java -jar roughcast.jar -i "2019-03-01T13:00:00Z/2019-05-11T15:30:00Z" -d 10 "48644c7a-975e-11e5-a090-c8e0eb18c1e9" "48cadf26-975e-11e5-b9c2-c8e0eb18c1e9"
```
For more details on usage you can run:
```
java -jar roughcast.jar --help
```
# About
This application is a simple CLI tool analyzing calendar data for multiple people and suggesting a time slot when they can all meet.

It was done as a test assignment, and I had to do it in a short amount of time. So I was cutting some corners. The code is not fully tested and there may be some bugs.