

1.To Run the MySql in Docker:
docker run --detach --env MYSQL_ROOT_PASSWORD=dummypassword --env MYSQL_USER=imrishuroy --env MYSQL_PASSWORD=123456 --env MYSQL_DATABASE=authentication-database --name mysql --publish 3306:3306 mysql:8-oracle
  
2.Add these properties in application.properties:

    spring.jpa.show-sql=true

    spring.datasource.url= jdbc:mysql://localhost:3306/authentication_database
    spring.datasource.username= imrishuroy
    spring.datasource.password= 123456
    
    spring.jpa.properties.hibernate.dialect= org.hibernate.dialect.MySQLDialect
    spring.jpa.hibernate.ddl-auto= update

3.To connect to the MySql in Docker:
\connect imrishuroy@localhost:3306  - imrishuroy is the username and 3306 is the port number

4.Give password for the user:

5.To enter into the SQL mode
\sql

6.Add the user roles in the table

    INSERT INTO roles(name) VALUES('ROLE_USER');
    INSERT INTO roles(name) VALUES('ROLE_MODERATOR');
    INSERT INTO roles(name) VALUES('ROLE_ADMIN');

7.To check the user roles in the table

    select * from roles;
                          
8.To check the users in the table

    select * from users;
      
9.To check the user roles in the table

    select * from user_roles;