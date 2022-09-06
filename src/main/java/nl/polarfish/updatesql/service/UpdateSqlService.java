package nl.polarfish.updatesql.service;

import static nl.polarfish.updatesql.util.FileUtilsExtension.readClasspathFileToString;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.SimplifiedLiquibase;
import liquibase.database.SimplifiedOfflineConnection;
import lombok.extern.slf4j.Slf4j;
import nl.polarfish.updatesql.exception.UpdateSqlException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UpdateSqlService {

    private final Map<String, String> supportedDbTypesMap = Map.of(
        "mssql", "mssql",
        "oracle", "oracle",
        "mysql", "mysql?version=5",
        "mysql5", "mysql?version=5",
        "mysql8", "mysql?version=8"
    );

    public String updateSql(String changeLogContent, String dbType) {

        validateDbType(dbType);

        boolean isXmlChangelog = changeLogContent.contains("<changeSet ");
        return updateSql(
            isXmlChangelog
                ? processChangeLogXml(changeLogContent)
                : wrapInDatabaseChangeLogYaml(changeLogContent),
            dbType,
            isXmlChangelog ? "changelog.xml" : "changelog.yml");
    }

    private void validateDbType(String dbType) {
        if (!supportedDbTypesMap.containsKey(dbType)) {
            throw new UpdateSqlException(
                "Requested database type is not supported. Supported types are: "
                + supportedDbTypesMap.keySet());
        }
    }

    private String updateSql(String changeLogContent, String dbType, String changeLogFileName) {

        Map<String, String> contentByFileName = new HashMap<>() {
            @Override
            public boolean containsKey(Object key) {
                return !String.valueOf(key).endsWith(".xsd");
            }

            @Override
            public String get(Object key) {
                return super.getOrDefault(key, String.format("-- Content placeholder (%s)", key));
            }
        };
        contentByFileName.put(changeLogFileName, changeLogContent);

        var resourceAccessor = new MyMockResourceAccessor(contentByFileName);
        var offlineConnection = new SimplifiedOfflineConnection(
            "offline:" + supportedDbTypesMap.get(dbType), resourceAccessor);

        try (Liquibase liquibase = new SimplifiedLiquibase(changeLogFileName, resourceAccessor, offlineConnection);
            StringWriter writer = new StringWriter()) {

            liquibase.update(new Contexts(), new LabelExpression(), writer, false);
            return writer.toString();
        } catch (Exception e) {
            log.info("Error", e);
            throw new UpdateSqlException("Failed to generate SQL from the provided change log", e);
        }
    }

    public String wrapInDatabaseChangeLogYaml(String changeLogContent) {
        return changeLogContent.contains("databaseChangeLog:")
            ? changeLogContent
            : ("databaseChangeLog:\n" + changeLogContent);
    }

    public String processChangeLogXml(String changeLogContent) {
        changeLogContent = wrapInDatabaseChangeLogXml(changeLogContent);
        changeLogContent = addPreDefinedPropertiesXml(changeLogContent);
        return changeLogContent;
    }

    public String wrapInDatabaseChangeLogXml(String changeLogContent) {
        return changeLogContent.contains("<databaseChangeLog")
            ? changeLogContent
            : ("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<databaseChangeLog "
                + "xmlns=\"http://www.liquibase.org/xml/ns/dbchangelog\" "
                + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
                + "xmlns:ext=\"http://www.liquibase.org/xml/ns/dbchangelog-ext\" "
                + "xsi:schemaLocation=\"http://www.liquibase.org/xml/ns/dbchangelog http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-4.7.xsd "
                + "http://www.liquibase.org/xml/ns/dbchangelog-ext http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-ext.xsd\">\n"
                + changeLogContent + "\n</databaseChangeLog>");
    }

    public String addPreDefinedPropertiesXml(String changeLogContent) {
        return changeLogContent.contains("<property ")
            ? changeLogContent
            : changeLogContent.replaceFirst("<changeSet ",
                "<property dbms=\"mysql\" name=\"autoIncrement\" value=\"true\"/>\n"
                    + "\n"
                    + "    <property dbms=\"mssql\" name=\"charDataType\" value=\"NCHAR\"/>\n"
                    + "    <property dbms=\"mysql\" name=\"charDataType\" value=\"CHAR\"/>\n"
                    + "    <property dbms=\"oracle\" name=\"charDataType\" value=\"CHAR\"/>\n"
                    + "\n"
                    + "    <property dbms=\"mssql\" name=\"varcharDataType\" value=\"NVARCHAR\"/>\n"
                    + "    <property dbms=\"mysql\" name=\"varcharDataType\" value=\"VARCHAR\"/>\n"
                    + "    <property dbms=\"oracle\" name=\"varcharDataType\" value=\"VARCHAR2\"/>\n"
                    + "\n"
                    + "    <property dbms=\"mssql\" name=\"fixedPointDataType\" value=\"DECIMAL\"/>\n"
                    + "    <property dbms=\"mysql\" name=\"fixedPointDataType\" value=\"DECIMAL\"/>\n"
                    + "    <property dbms=\"oracle\" name=\"fixedPointDataType\" value=\"NUMBER\"/>\n"
                    + "\n"
                    + "    <property dbms=\"mssql\" name=\"uuidDataType\" value=\"UNIQUEIDENTIFIER\"/>\n"
                    + "    <property dbms=\"mysql\" name=\"uuidDataType\" value=\"VARBINARY(16)\"/>\n"
                    + "    <property dbms=\"oracle\" name=\"uuidDataType\" value=\"RAW(16)\"/>\n"
                    + "\n"
                    + "    <property dbms=\"mssql\" name=\"clobDataType\" value=\"NVARCHAR(MAX)\"/>\n"
                    + "    <property dbms=\"mysql\" name=\"clobDataType\" value=\"LONGTEXT\"/>\n"
                    + "    <property dbms=\"oracle\" name=\"clobDataType\" value=\"CLOB\"/>\n"
                    + "\n"
                    + "    <property dbms=\"mssql\" name=\"dateTimeDataType\" value=\"DATETIME2\"/>\n"
                    + "    <property dbms=\"mysql\" name=\"dateTimeDataType\" value=\"DATETIME\"/>\n"
                    + "    <property dbms=\"oracle\" name=\"dateTimeDataType\" value=\"TIMESTAMP\"/>\n"
                    + "\n"
                    + "    <property dbms=\"mssql\" name=\"timestampDataType\" value=\"DATETIME2\"/>\n"
                    + "    <property dbms=\"mysql\" name=\"timestampDataType\" value=\"DATETIME\"/>\n"
                    + "    <property dbms=\"oracle\" name=\"timestampDataType\" value=\"TIMESTAMP\"/>\n"
                    + "\n"
                    + "    <property dbms=\"mysql\" name=\"db.type\" value=\"mysql\"/>\n"
                    + "    <property dbms=\"mssql\" name=\"db.type\" value=\"mssql\"/>\n"
                    + "    <property dbms=\"oracle\" name=\"db.type\" value=\"oracle\"/>\n<changeSet ");
    }

    @PostConstruct
    private void preWarm() {
        log.info("Pre-warming UpdateSqlService started");
        long start = System.currentTimeMillis();
        try {
            updateSql(readClasspathFileToString("pre-warming-changelog.yml"), "mysql");
            log.info("Pre-warming UpdateSqlService finished (took {} seconds)",
                ((float) (System.currentTimeMillis() - start)) / 1000);
        } catch (Exception e) {
            log.warn("Pre-warming UpdateSqlService failed", e);
        }
    }

}
