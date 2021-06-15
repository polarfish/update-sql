# update-sql

[![Build](https://github.com/polarfish/update-sql/workflows/Build/badge.svg)](https://github.com/polarfish/update-sql/actions?query=workflow%3ABuild)

Liquibase updateSQL command as a service.  

## Description  
This service is useful for projects that treat Liquibase changelog 
as single source of truth but deliver plain SQL files as well.  
So, when developers finish updating the changelog, they can easily generate
the corresponding SQL files.  
  
The result of running this service should be equal to the CLI command below:
```shell script
/path/to/liquibase-bin/liquibase --url=offline:<db-type> --changeLogFile=/path/to/changelog.yml updateSQL
```

## Limitations
The service:
- currently, supports YAML and XML changelogs (not JSON)
- generates SQL for MySql, MariaDB, MSSQL and Oracle databases
- replaces external SQL files inclusion with `-- Content placeholder (<file-name>)`
- does not support external changelogs inclusion (will result in Bad Request)

## Running

### Docker

#### AMD64

```shell script
docker run -d -p 8080:8080 polarfish/update-sql:1.1.0-amd64
```

#### ARM64

```shell script
docker run -d -p 8080:8080 polarfish/update-sql:1.1.0-arm64
```

### JAR
```shell script
mvn clean package && java -jar target/update-sql*.jar
```

### spring-boot-maven-plugin
```shell script
mvn spring-boot:run
```

## Screenshots
![image](https://user-images.githubusercontent.com/1070579/81563018-01a21b80-9396-11ea-94ad-d5f3d11c35e7.png)