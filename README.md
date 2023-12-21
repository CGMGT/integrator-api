### Backend Integrador Home
###### Version 1.0

Servicios REST para Integrador Home.

## Configuration
- Spring Boot 2.7.1
- Java 1.8 
- Maven 3.6.3

## Dependencies
- JPA
- Web
- AOP

## Usage
#### Clone repository
```
git clone https://github.com/CGMGT/integrator-api/src.git
```
#### Project information
```
    <groupId>gt.com.tigo</groupId>
    <artifactId>integrador-home-api</artifactId>
    <version>1.0</version>
    <packaging>war</packaging>
    <name>integrador-home-api</name>
    <description>Tigo Integrador Home API</description>
```
#### Base package
```
    gt.com.tigo.integradorhome
```
#### Main class
```
    public class IntegradorHomeApplication extends SpringBootServletInitializer implements WebApplicationInitializer {
        ...
    }
```
#### Main class path
```
    <project-path>/src/main/java/gt/com/tigo/integradorhome/IntegradorHomeApplication.java
```
#### Weblogic configuration
```
    <wls:context-root>/integrador-home-api</wls:context-root>
```
#### Build
```
mvn package
```
##### API Documentation
```
N/A
``` 
