XML Parser Application

Overview

This application parses XML files, processes data for municipalities and municipality parts, and stores the information in an H2 database.

Features

	•	File Downloading and Unzipping: Downloads and extracts ZIP files.
	•	XML Parsing: Parses XML documents to extract municipality data.
	•	Data Persistence: Stores extracted data into an H2 database.
	•	H2 Database Console: Provides an H2 database console for direct database interactions.

Requirements

	•	Docker and Docker Compose
	•	Java 17+
	•	Maven

Setup
1.	Clone the repository:
```
git clone https://github.com/your-username/xml-parser.git
cd xml-parser
```
2.	Build the project:
```
mvn clean package
```
3.	Run the application with Docker:
```
docker compose up --build
```

4.	Access H2 Console:
Navigate to http://localhost:8080/h2-console. Use the following credentials:
```
•	JDBC URL: jdbc:h2:mem:testdb
•	Username: sa
•	Password: (leave empty)
```

Troubleshooting

	•	FileNotFoundException: Ensure that the file paths in application.properties are correct.
	•	Docker Issues: Ensure Docker and Docker Compose are correctly installed and running.
