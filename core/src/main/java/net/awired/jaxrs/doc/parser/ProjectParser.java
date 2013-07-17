/**
 *
 *     Copyright (C) Awired.net
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
package net.awired.jaxrs.doc.parser;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.ws.rs.Path;
import net.awired.jaxrs.doc.DocConfig;
import net.awired.jaxrs.doc.domain.ApiDefinition;
import net.awired.jaxrs.doc.domain.ProjectDefinition;
import org.reflections.Reflections;

public class ProjectParser {

    private final DocConfig config;

    public ProjectParser(DocConfig config) {
        this.config = config;
    }

    public ProjectDefinition parse() {
        ProjectDefinition project = new ProjectDefinition();

        Set<Class<?>> discoverResources = discoverResources(config.getProjectPackagesToScan());

        for (Class<?> resource : discoverResources) {
            ApiDefinition api = config.getApiParser().parse(project, resource);
            project.getApis().add(api);
        }

        return project;
    }

    private Set<Class<?>> discoverResources(List<String> packages) {
        Set<Class<?>> classes = new HashSet<>();

        Reflections reflections = new Reflections(packages); // TODO do it by hand to limit dependencies
        classes.addAll(reflections.getTypesAnnotatedWith(Path.class));
        return classes;
    }

}
