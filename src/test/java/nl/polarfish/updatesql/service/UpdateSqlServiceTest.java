package nl.polarfish.updatesql.service;

import static nl.polarfish.updatesql.util.FileUtilsExtension.readClasspathFileToString;
import static org.hamcrest.core.Is.is;

import java.nio.file.Paths;
import java.util.List;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;

class UpdateSqlServiceTest {

    private final UpdateSqlService liquibaseService = new UpdateSqlService();

    @Test
    void updateSql_basic() {
        testUpdateSql("changelog/basic.yml");
    }

    @Test
    void updateSql_complex() {
        testUpdateSql("changelog/complex.yml");
    }

    private void testUpdateSql(String changeLogPath) {
        List.of("mysql", "mssql", "oracle").forEach(dbType ->
            {
                String fileName = Paths.get(changeLogPath).getFileName().toString();
                MatcherAssert.assertThat(
                    String.format("Changelog %s should generate right SQL for %s",
                        fileName, dbType),
                    liquibaseService.updateSql(readClasspathFileToString(changeLogPath), dbType),
                    is(readClasspathFileToString(String.format("sql/%s/%s", dbType, fileName.replace("yml", "sql")))));
            }
        );
    }

}