# Getting started

In order to get the project running:

1. git clone it
2. Install MySQL (or a preferred database)
3. Install Maven 
4. Put a copy of config/scipub.properties into /config (or c:\config)
5. Configure your database credentials in the newly created scipub.properties 
6. Go to the project directory
7. mvn tomcat7:run

(alternative to /config, you can specify MAVEN_OPTS environment variable to contain -Dscipub.config.location=/path/to/config)
