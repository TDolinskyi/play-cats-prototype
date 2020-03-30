Summary

This project is a demonstration of using:

- Scala;
- Cats-effect;
- Slick
- PostgresSQL
- Docker-compose
- Guice


To run application docker, jdk, sbt needs to be install. To run application follow next steps:

1. Change config: 
```
onStart = {
   createDatabase = true
   createTables = true
}
```

2. Run docker-compose up;
3. When image started open terminal in project folder and type next command:
```
sbt run
```
4. When application started in browser open http://localhost:9000/db - to create DB and tables;
5. After schema created change config back to false and restart application;
```
onStart = {
   createDatabase = true
   createTables = true
}
```
6. Now cats-scala=prototype up and running;