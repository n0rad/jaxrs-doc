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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import fr.norad.jaxrs.doc.JaxrsDocProcessorFactory;
import fr.norad.jaxrs.doc.domain.ApiDefinition;
import fr.norad.jaxrs.doc.domain.OperationDefinition;
import fr.norad.jaxrs.doc.domain.ProjectDefinition;
import fr.norad.jaxrs.doc.parserapi.ApiParser;
import lombok.Getter;

public class ApiProcessor {
    private final JaxrsDocProcessorFactory factory;
    @Getter
    private final Set<ApiParser> apiParsers = new LinkedHashSet<>();

    public ApiProcessor(JaxrsDocProcessorFactory factory, Collection<ApiParser> apiParsers) {
        this.factory = factory;
        this.apiParsers.addAll(apiParsers);
    }

    public ApiDefinition process(ProjectDefinition project, Class<?> apiClass) {
        ApiDefinition api = new ApiDefinition();
        for (ApiParser parser : apiParsers) {
            parser.parse(api, apiClass);

            Set<Method> operations = parser.findOperations(apiClass);
            if (operations == null) {
                continue;
            }

            for (Method method : operations) {
                OperationDefinition operation = factory.getOperationProcessor().process(project, api, method);
                if (api.getOperations() == null) {
                    api.setOperations(new ArrayList<OperationDefinition>());
                }
                api.getOperations().add(operation);
            }
        }

        return api;
    }
}
