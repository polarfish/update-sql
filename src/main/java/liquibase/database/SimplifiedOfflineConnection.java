package liquibase.database;

import liquibase.changelog.ChangeLogHistoryService;
import liquibase.changelog.DoNothingChangeLogHistoryService;
import liquibase.resource.ResourceAccessor;

public class SimplifiedOfflineConnection extends OfflineConnection {

    public SimplifiedOfflineConnection(String url, ResourceAccessor resourceAccessor) {
        super(url, resourceAccessor);
    }

    @Override
    protected ChangeLogHistoryService createChangeLogHistoryService(Database database) {
        return new DoNothingChangeLogHistoryService();
    }

}
