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
package fr.norad.jaxrs.doc.parser;

import java.lang.reflect.Method;
import java.util.Set;
import javax.ws.rs.Path;
import org.reflections.ReflectionUtils;
import fr.norad.jaxrs.doc.DocConfig;
import fr.norad.jaxrs.doc.domain.ApiDefinition;
import fr.norad.jaxrs.doc.domain.OperationDefinition;
import fr.norad.jaxrs.doc.domain.ProjectDefinition;
import fr.norad.jaxrs.doc.utils.AnnotationUtil;

public class ApiParser {

    private final DocConfig config;

    public ApiParser(DocConfig docConfig) {
        this.config = docConfig;
    }

    public ApiDefinition parse(ProjectDefinition project, Class<?> apiClass) {
        ApiDefinition api = new ApiDefinition();
        api.setResourceClass(apiClass);

        Path annotation = AnnotationUtil.findAnnotation(apiClass, Path.class);
        api.setPath(annotation.value());

        @SuppressWarnings("unchecked")
        Set<Method> methods = ReflectionUtils.getAllMethods(apiClass);
        for (Method method : methods) {
            if (config.getOperationParser().isOperation(method)) {
                OperationDefinition operation = config.getOperationParser().parse(project, api, method);
                api.getOperations().add(operation);
            }
        }
        return api;
    }

}
