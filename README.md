# Simple Booking

### Requires:
OpenJdk 23 (or change Java version in pom.xml to 23, 21 or 17)  
MySQL  
Maven (Optional)  
  
## To Run
1. Log into MySQL as root from console  
  `mysql -u root -p`  
   Enter Password
2. Create "simple_booking" Database  
  `CREATE DATABASE simple_booking;`  
4. Create User "springuser" with password "7529"  
  `CREATE USER 'springuser'@'localhost' IDENTIFIED BY '7529';`
5. Manage Privileges  
  `GRANT ALL PRIVILEGES ON simple_booking.* TO 'springuser'@'localhost';`  
  `FLUSH PRIVILEGES;`
6. To start server  
  open terminal to `/Simple-Booking/simpleBooking/` and run the following command:  
  `mvn spring-boot:run`
7. In a Web Browser go to `localhost:8080`  
If port 8080 is in use stop the running program or add to application.properties  
`server.port = <port number>`   
then go to  `localhost:<port number>`


## Add User to DB for authentication and Login
1. Enter `localhost:<port number>/addEmployee`  
2. Login with username: `emp001` and password: `john001`

