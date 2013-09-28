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
package fr.norad.jaxrs.doc.parser.jaxrs;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import org.reflections.ReflectionUtils;
import fr.norad.jaxrs.doc.DocConfig;
import fr.norad.jaxrs.doc.annotations.Outdated;
import fr.norad.jaxrs.doc.domain.ApiDefinition;
import fr.norad.jaxrs.doc.domain.OperationDefinition;
import fr.norad.jaxrs.doc.domain.ProjectDefinition;
import fr.norad.jaxrs.doc.utils.AnnotationUtil;

public class JaxrsApiParser {

    private final DocConfig config;

    public JaxrsApiParser(DocConfig docConfig) {
        this.config = docConfig;
    }

    public ApiDefinition parse(ProjectDefinition project, Class<?> apiClass) {
        ApiDefinition api = new ApiDefinition();
        api.setResourceClass(apiClass);

        Path annotation = AnnotationUtil.findAnnotation(apiClass, Path.class);
        api.setPath(annotation.value());

        Deprecated deprecated = AnnotationUtil.findAnnotation(apiClass, Deprecated.class);
        api.setDeprecated(deprecated != null ? true : null);

        Outdated outdated = AnnotationUtil.findAnnotation(apiClass, Outdated.class);
        if (outdated != null) {
            api.setDeprecated(true);
            api.setDeprecatedCause(outdated.cause());
            api.setDeprecatedSince(outdated.since().isEmpty() ? null : outdated.since());
        }

        @SuppressWarnings("unchecked")
        Set<Method> methods = ReflectionUtils.getAllMethods(apiClass);
        for (Method method : methods) {
            if (config.getOperationParser().isOperation(method)) {
                OperationDefinition operation = config.getOperationParser().parse(project, api, method);
                api.getOperations().add(operation);
            }
        }

        Consumes consumes = AnnotationUtil.findAnnotation(apiClass, Consumes.class);
        if (consumes != null) {
            for (String consume : consumes.value()) {
                if (api.getConsumes() == null) {
                    api.setConsumes(new ArrayList<String>());
                }
                api.getConsumes().add(consume);
            }
        }

        Produces produces = AnnotationUtil.findAnnotation(apiClass, Produces.class);
        if (produces != null) {
            for (String produce : produces.value()) {
                if (api.getProduces() == null) {
                    api.setProduces(new ArrayList<String>());
                }
                api.getProduces().add(produce);
            }
        }

        return api;
    }

}
