1. # How to Run the Service
1.1 Requirements
Ensure your environment meets the following conditions:
JDK 17+
Maven 3.6+
Spring Boot 3+

1.2 Build and Run
Clone the code repository and execute the following commands in the project directory:
It is recommended to use idea to start the project

1.3 Static resources:
Place the product.csv file in the resources/static directory. If modification is needed, please follow the format: productId,productName.
# Compile the project
mvn clean package
# Run the Spring Boot application
mvn spring-boot:run
By default, the service will run at **http://localhost:8080/**.

2. # How to Use the API
2.1 Upload trade.csv for data processing. The trade.csv format should be: date,productId,currency,price

2.2 Call the API for data processing
File upload:
Use a POST request to the /api/trades/enrich endpoint to upload the trade.csv file.
Example request:
curl -X POST "http://localhost:8080/TradeEnrichmentController/enrich" \
-H "Content-Type: multipart/form-data" \
-F "file=@trade.csv"
The AWS online version can be directly called: http://43.201.28.13:8080/TradeEnrichmentController/enrich

2.3 Response Results:
Success: 200 OK
Error: 400 Bad Request

3. # Code Limitations

3.1 Current Limitations
File size limit: The uploaded trade.csv file cannot exceed 50MB.
Strict data format: The CSV file must contain 4 columns (date, productId, currency, price), otherwise the request will be rejected.
Skip invalid date data: If the date format in the CSV file is incorrect, that data row will be skipped.

3.2 How to Modify Limitations
To adjust the file size limit, please modify application.properties:
spring.servlet.multipart.max-file-size=50MB
spring.servlet.multipart.max-request-size=50MB

4. # Design Discussions/Comments
4.1 Code Structure
TradeEnrichmentController.java is responsible for receiving and processing HTTP requests.
TradeEnrichmentService.java is responsible for parsing CSV and validating data.
GlobalExceptionHandler.java handles errors uniformly and returns HTTP responses.
model/Trade.java defines the Trade transaction object.

4.2 Design Approach
Uses Spring Boot + REST API to ensure extensibility.
Global exception handling to avoid writing repetitive try-catch in the Controller layer.
Data validation logic is placed in TradeService to keep the Controller concise.

5. # What improvements could be made with more time?
Database storage:
Currently, data is only processed and returned, without storage. If stored in a database, it could support historical queries, data statistics, and other functions.

Add Swagger documentation:
Currently, API documentation needs to be written manually. It is recommended to integrate SpringDoc OpenAPI to automatically generate API documentation.

Support more data formats:
Currently only CSV is supported. In the future, JSON, Excel files, etc. could be supported.