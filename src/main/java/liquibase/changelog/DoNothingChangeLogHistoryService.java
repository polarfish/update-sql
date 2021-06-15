package liquibase.changelog;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.changelog.ChangeSet.ExecType;
import liquibase.changelog.ChangeSet.RunStatus;
import liquibase.database.Database;

public class DoNothingChangeLogHistoryService implements ChangeLogHistoryService {

    @Override
    public boolean supports(Database database) {
        return true;
    }

    @Override
    public void setDatabase(Database database) {

    }

    @Override
    public void reset() {

    }

    @Override
    public void init() {

    }

    @Override
    public void upgradeChecksums(DatabaseChangeLog databaseChangeLog, Contexts contexts, LabelExpression labels) {

    }

    @Override
    public List<RanChangeSet> getRanChangeSets() {
        return Collections.emptyList();
    }

    @Override
    public RanChangeSet getRanChangeSet(ChangeSet changeSet) {
        return null;
    }

    @Override
    public RunStatus getRunStatus(ChangeSet changeSet) {
        return RunStatus.ALREADY_RAN;
    }

    @Override
    public Date getRanDate(ChangeSet changeSet) {
        return new Date();
    }

    @Override
    public void setExecType(ChangeSet changeSet, ExecType execType) {

    }

    @Override
    public void removeFromHistory(ChangeSet changeSet) {

    }

    @Override
    public int getNextSequenceValue() {
        return 0;
    }

    @Override
    public void tag(String tagString) {

    }

    @Override
    public boolean tagExists(String tag) {
        return false;
    }

    @Override
    public void clearAllCheckSums() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public String getDeploymentId() {
        return null;
    }

    @Override
    public void resetDeploymentId() {

    }

    @Override
    public void generateDeploymentId() {

    }

    @Override
    public int getPriority() {
        return 100;
    }
}
