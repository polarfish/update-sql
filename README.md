# update-sql

Liquibase updateSQL command as a service.  

## Description  
This service is useful for projects that treat Liquibase changelog 
as single source of truth but deliver plain SQL files as well.  
So, when developers finish updating the changelog, they can easily generate
the corresponding SQL files.  
  
The result of running this service should be equal to the CLI command below:
```shell script
/path/to/liquibase-3.8.8-bin/liquibase --url=offline:<db-type> --changeLogFile=/path/to/changelog.yml updateSQL
```

## Limitations
The service:
- currently supports only YAML changelogs (not JSON nor XML)
- generates SQL for MySql, MSSQL and Oracle databases
- replaces external SQL files inclusion with `-- Content placeholder (<file-name>)`
- does not support external changelogs inclusion (will result in Bad Request)

## Screenshots
![image](https://user-images.githubusercontent.com/1070579/80415567-044f3c00-88d3-11ea-9aa7-1bfb3dc58a41.png)