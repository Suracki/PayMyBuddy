<!-- ABOUT THE PROJECT -->
## About The Project

PayMyBuddy is a fund transfer application. This project is the backend part of that web application.
 

<p align="right">(<a href="#top">back to top</a>)</p>



### Built With

* Java
* Spring Boot
* Maven
* GSON
* Swagger
* JUnit
* Log4j
* JaCoCo

<p align="right">(<a href="#top">back to top</a>)</p>



<!-- GETTING STARTED -->
## Getting Started

### Installation

1. Clone the repo
   ```sh
   git clone https://github.com/suracki/paymybuddy.git
   ```
2. Install & configure MySQL
   ```available from https://dev.mysql.com/downloads/installer/
   databaseSetup.sql contains database setup scripts for Prod and Test environments
   prodDummyData.sql contains dummy data that can be inserted for manual testing if desired
   Once configured, create two System Environment Variables to contain the MySQL username and password you wish the app to use
   ```
3. Set configuration variables in `application.properties` as desired
	```variables available:
   sql.user.varname -> the name of the System Environment Variable containing the username for the MySQL database
   sql.user.password -> the name of the System Environment Variable containing the password for the MySQL database
   sql.url -> the URL for connecting to the MySQL database
   payment.process.schedule.rate -> the frequency with which the system should query the bank interface for payment processing
   frontend.app.ip -> the IP address of the machine the frontend of the app will connect from, for security purposes   
   ```
4. Start the application running by using PaymybuddyApplication
	```Once the app is running, Swagger can be used to access the API:
	http://localhost:8080/swagger-ui/#/
	```

<p align="right">(<a href="#top">back to top</a>)</p>



<!-- USAGE EXAMPLES -->
## Usage

Once the application is running, it can be accessed by default at http://localhost:8080/

To view available endpoints, please access the Swagger UI at http://localhost:8080/swagger-ui/#/

App configuration settings can be adjusted in application.properties file as descriped in Installation steps

<p align="right">(<a href="#top">back to top</a>)</p>

<!-- DATABASE DIAGRAM -->
## Database Diagram

The database created by running the provided scripts will match this schema:

![Database UML](https://github.com/Suracki/PayMyBuddy/blob/mockbank-branch/DatabaseDiagram.jpg?raw=true)

<p align="right">(<a href="#top">back to top</a>)</p>


<!-- CONTACT -->
## Contact

Simon Linford - simon.linford@gmail.com

Project Link: [https://github.com/suracki/paymybuddy](https://github.com/suracki/paymybuddy)

<p align="right">(<a href="#top">back to top</a>)</p>

