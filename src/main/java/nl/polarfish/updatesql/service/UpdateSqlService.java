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
import liquibase.resource.ResourceAccessor;
import liquibase.sdk.resource.MockResourceAccessor;
import lombok.extern.slf4j.Slf4j;
import nl.polarfish.updatesql.exception.UpdateSqlException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UpdateSqlService {

    public static final String DATABASE_CHANGE_LOG_COLON = "databaseChangeLog:";

    public String updateSql(String changeLogContent, String dbType) {
        try {
            String changeLogFile = "changelog.yml";
            Map<String, String> contentByFileName = new HashMap<>() {
                @Override
                public boolean containsKey(Object key) {
                    return true;
                }

                @Override
                public String get(Object key) {
                    return super.getOrDefault(key, String.format("-- Content placeholder (%s)", key));
                }
            };
            contentByFileName.put(
                changeLogFile,
                changeLogContent.contains(DATABASE_CHANGE_LOG_COLON)
                    ? changeLogContent
                    : (DATABASE_CHANGE_LOG_COLON + "\n" + changeLogContent));
            ResourceAccessor resourceAccessor = new MockResourceAccessor(contentByFileName);
            Liquibase liquibase = new SimplifiedLiquibase(
                changeLogFile, resourceAccessor,
                new SimplifiedOfflineConnection("offline:" + dbType, resourceAccessor));
            StringWriter writer = new StringWriter();
            liquibase.update(new Contexts(), new LabelExpression(), writer, false);
            return writer.toString();
        } catch (Exception e) {
            throw new UpdateSqlException("Failed to generate SQL from the provided change log", e);
        }
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
