/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
