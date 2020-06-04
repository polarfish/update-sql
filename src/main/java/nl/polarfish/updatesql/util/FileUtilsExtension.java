package nl.polarfish.updatesql.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.FileUtils;

public class FileUtilsExtension {

    /**
     * Reads classpath file content to a string. Wraps checked exceptions into runtime exceptions.
     *
     * @param path file path
     * @return content of the file
     */
    public static String readClasspathFileToString(String path) {
        try {
            URL resource = FileUtilsExtension.class.getClassLoader().getResource(path);

            if (resource == null) {
                throw new FileNotFoundException("File '" + path + "' does not exist");
            }

            return FileUtils.readFileToString(
                new File(resource.getFile()),
                StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
