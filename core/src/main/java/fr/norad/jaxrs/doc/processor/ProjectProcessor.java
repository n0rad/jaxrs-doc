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
package fr.norad.jaxrs.doc.processor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import fr.norad.jaxrs.doc.JaxrsDocProcessorFactory;
import fr.norad.jaxrs.doc.domain.ApiDefinition;
import fr.norad.jaxrs.doc.domain.ProjectDefinition;
import fr.norad.jaxrs.doc.parserapi.ProjectParser;

public class ProjectProcessor {

    private final Set<ProjectParser> parsers = new LinkedHashSet<>();
    private final JaxrsDocProcessorFactory factory;

    public ProjectProcessor(JaxrsDocProcessorFactory factory, Collection<ProjectParser> parsers) {
        this.factory = factory;
        this.parsers.addAll(parsers);
    }

    public ProjectDefinition process() {
        ProjectDefinition project = new ProjectDefinition();
        for (ProjectParser parser : parsers) {
            parser.parse(project);
            List<Class<?>> apiClasses = parser.apiClasses();
            for (Class<?> apiClass : apiClasses) {
                ApiDefinition api = factory.getApiProcessor().process(project, apiClass);
                if (project.getApis() == null) {
                    project.setApis(new ArrayList<ApiDefinition>());
                }
                project.getApis().add(api);
            }
        }
        return project;
    }

}
