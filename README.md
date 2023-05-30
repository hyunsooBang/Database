

# Course Evaluation Program

This Java program interacts with a MySQL database to perform various operations related to course evaluation.


## Team Information

- Team Name: SQLSamurais

### Team Members

1. **Name:** 조유담
   - **Student ID:** 1942051

2. **Name:** 방현수
   - **Student ID:** 1916011

3. **Name:** 이서연
   - **Student ID:** 2176255

## Prerequisites

- Java Development Kit (JDK) installed
- MySQL database server running
- JDBC driver for MySQL

## Setup

1. Clone the repository or download the source code files.
2. Open the project in your preferred Java development environment.
3. Include the JDBC driver for MySQL in the project's classpath.
4. modify Welcome.java 51 line to "jdbc:mysql://localhost:3306/database name", "root", "root비밀번호" 
* When you do not modify welcome.java (number.4) , you can not connect MySQL DB. *

To run the JAR file, you can use either of the following methods:

1. Open a terminal or command prompt, navigate to the directory containing the JAR file, and execute the following command:
java -jar filename.jar

2. Alternatively, you can double-click the JAR file to execute it.

## Usage

1. Run the `Welcome` class, which contains the main method, to start the program.
2. The program will establish a connection with the MySQL database.
3. The program will retrieve data from the "student" table in the database and populate a list of User objects.
4. Enter your name and number when prompted for verification.
5. The program will validate your information by checking if it matches any user in the list.
6. Once validated, a menu will be displayed with different options for course evaluation operations.
7. Choose an option from the menu to perform the desired operation:
   - Insert a new course evaluation
   - Search for a course evaluation
   - Modify a course evaluation
   - Delete a course evaluation
   - View user information
   - Exit the program
8. Follow the prompts and enter the required information to complete each operation.
9. The program will interact with the MySQL database using SQL queries to perform the necessary operations.
10. The menu will be displayed after each operation until you choose to exit the program.


Please adjust the instructions and details based on your specific code and environment requirements.
