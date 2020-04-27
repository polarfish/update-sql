package nl.polarfish.updatesql.service;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.SimplifiedLiquibase;
import liquibase.database.SimplifiedOfflineConnection;
import liquibase.resource.ResourceAccessor;
import liquibase.sdk.resource.MockResourceAccessor;
import nl.polarfish.updatesql.exception.UpdateSqlException;
import org.springframework.stereotype.Service;

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

}
