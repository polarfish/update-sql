package nl.polarfish.updatesql.service;

import static org.hamcrest.core.Is.is;

import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.List;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
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
                    liquibaseService.updateSql(readFileToString(changeLogPath), dbType),
                    is(readFileToString(String.format("sql/%s/%s", dbType, fileName.replace("yml", "sql")))));
            }
        );
    }

    @SneakyThrows
    public static String readFileToString(String path) {
        URL resource = UpdateSqlServiceTest.class.getClassLoader().getResource(path);

        if (resource == null) {
            throw new RuntimeException(String.format("Failed to load file [%s]", path));
        }

        return FileUtils.readFileToString(
            new File(resource.getFile()),
            StandardCharsets.UTF_8);
    }
}