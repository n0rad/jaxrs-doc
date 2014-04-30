/**
 *
 *     Copyright (C) norad.fr
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *             http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */
package fr.norad.jaxrs.doc.parser;

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Path;
import org.reflections.Reflections;
import fr.norad.jaxrs.doc.api.domain.ProjectDefinition;
import fr.norad.jaxrs.doc.parserapi.ProjectParser;

public class ProjectDiscoveryParser implements ProjectParser {

    private final List<String> packagesToScan;
    private final String name;
    private final String version;

    public ProjectDiscoveryParser(List<String> packagesToScan, String name, String version) {
        this.packagesToScan = packagesToScan;
        this.name = name;
        this.version = version;
    }

    @Override
    public void parse(ProjectDefinition project) {
        project.setName(name);
        project.setVersion(version);
    }

    @Override
    public List<Class<?>> apiClasses() {
        List<Class<?>> classes = new ArrayList<>();
        Reflections reflections = new Reflections(packagesToScan); // TODO do it by hand to limit dependencies
        classes.addAll(reflections.getTypesAnnotatedWith(Path.class));
        return classes;
    }

}
