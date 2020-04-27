package liquibase;

import liquibase.changelog.DatabaseChangeLog;
import liquibase.database.DatabaseConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ResourceAccessor;

public class SimplifiedLiquibase extends Liquibase {

    public SimplifiedLiquibase(String changeLogFile, ResourceAccessor resourceAccessor,
        DatabaseConnection conn) throws LiquibaseException {
        super(changeLogFile, resourceAccessor, conn);
    }

    @Override
    public void checkLiquibaseTables(boolean updateExistingNullChecksums, DatabaseChangeLog databaseChangeLog,
        Contexts contexts, LabelExpression labelExpression) {
    }


    @Override
    public void outputHeader(String message) {
    }
}
