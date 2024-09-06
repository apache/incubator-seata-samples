package org.apache.seata.util;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author jingliu_xiong@foxmail.com
 */
public class MavenUtils {
    public static boolean hasSubModule(File pomFile) {
        MavenXpp3Reader reader = new MavenXpp3Reader();
        try (FileReader fileReader = new FileReader(pomFile)) {
            Model model = reader.read(fileReader);
            return model.getModules() != null && !model.getModules().isEmpty();
        } catch (IOException | XmlPullParserException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static File getPomFile(File moduleDir) {
        if (moduleDir == null) {
            return null;
        }
        for (File file : moduleDir.listFiles()) {
            if (file.getName().equals("pom.xml")) {
                return file;
            }
        }
        return null;
    }
}
