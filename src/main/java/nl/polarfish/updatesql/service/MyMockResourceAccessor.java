package nl.polarfish.updatesql.service;

import java.io.IOException;
import java.util.Map;
import liquibase.resource.InputStreamList;
import liquibase.sdk.resource.MockResourceAccessor;

public class MyMockResourceAccessor extends MockResourceAccessor {

    public MyMockResourceAccessor(Map<String, String> contentByFileName) {
        super(contentByFileName);
    }

    @Override
    public InputStreamList openStreams(String relativeTo, String streamPath) throws IOException {
        InputStreamList inputStreams = super.openStreams(relativeTo, streamPath);

        if (inputStreams == null) {
            inputStreams = new InputStreamList();
        }

        return inputStreams;
    }
}
