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
import java.util.List;
import fr.norad.jaxrs.doc.JaxrsDocProcessorFactory;
import fr.norad.jaxrs.doc.ParserHolder;
import fr.norad.jaxrs.doc.domain.ApiDefinition;
import fr.norad.jaxrs.doc.domain.ProjectDefinition;
import fr.norad.jaxrs.doc.parserapi.ProjectParser;
import lombok.Getter;

public class ProjectProcessor {
    private final JaxrsDocProcessorFactory factory;
    @Getter
    private ParserHolder<ProjectParser> parsers;

    public ProjectProcessor(JaxrsDocProcessorFactory factory, ParserHolder<ProjectParser> parsers) {
        this.factory = factory;
        this.parsers = parsers;
    }

    public ProjectDefinition process() {
        ProjectDefinition project = new ProjectDefinition();
        for (ProjectParser parser : parsers.get()) {
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
