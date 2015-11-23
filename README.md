# Getting started

In order to get the project running:

1. git clone it
2. Install MySQL (or a preferred database)
3. Install Maven 
4. Put a copy of config/scipub.properties into /config (or c:\config)
5. Create an empty database with your desired name (scipub by default) and with UTF-8 as character set
6. Configure your database credentials in the newly created scipub.properties
7. Go to the project directory
8. Download and add to the lib folder the 3rd party jar files listed vuze-jar.txt
9. Optionally, install pandoc (http://pandoc.org/) - needed to handle uploaded files conversion  
10. mvn tomcat7:run
11. Stop the service (CTRL+C) - the database tables are now automatically created
12. Import the science_branches.sql into the database
13. mvn tomcat7:run
14. Navigate your browser to http://localhost:8080/scipub

(alternative to /config, you can specify MAVEN_OPTS environment variable to contain -Dscipub.config.location=/path/to/config)

(The Vuze jar is too big and for the time being is kept outside the repo. It can be downloaded from here http://cf1.vuze.com/site/dev/files/Vuze_5501-31.jar)