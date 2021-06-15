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
import liquibase.sdk.resource.MockResourceAccessor;
import lombok.extern.slf4j.Slf4j;
import nl.polarfish.updatesql.exception.UpdateSqlException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UpdateSqlService {

    public String updateSql(String changeLogContent, String dbType) {

        boolean isXmlChangelog = changeLogContent.contains("<changeSet ");
        return updateSql(
            isXmlChangelog
                ? wrapInDatabaseChangeLogXml(changeLogContent)
                : wrapInDatabaseChangeLogYaml(changeLogContent),
            dbType,
            isXmlChangelog ? "changelog.xml" : "changelog.yml");
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

        var resourceAccessor = new MockResourceAccessor(contentByFileName);
        var offlineConnection = new SimplifiedOfflineConnection("offline:" + dbType, resourceAccessor);

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

    public String wrapInDatabaseChangeLogXml(String changeLogContent) {
        return changeLogContent.contains("<databaseChangeLog")
            ? changeLogContent
            : ("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<databaseChangeLog "
                + "xmlns=\"http://www.liquibase.org/xml/ns/dbchangelog\" "
                + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
                + "xmlns:ext=\"http://www.liquibase.org/xml/ns/dbchangelog-ext\" "
                + "xsi:schemaLocation=\"http://www.liquibase.org/xml/ns/dbchangelog http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-3.10.xsd "
                + "http://www.liquibase.org/xml/ns/dbchangelog-ext http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-ext.xsd\">\n"
                + changeLogContent + "\n</databaseChangeLog>");
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
