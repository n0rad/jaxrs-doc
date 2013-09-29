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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.Consumes;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import org.reflections.ReflectionUtils;
import fr.norad.jaxrs.doc.domain.ApiDefinition;
import fr.norad.jaxrs.doc.parserapi.ApiParser;
import fr.norad.jaxrs.doc.utils.AnnotationUtil;

public class ApiJaxrsParser implements ApiParser {

    @Override
    public void parse(ApiDefinition api, Class<?> apiClass) {
        Path path = AnnotationUtil.findAnnotation(apiClass, Path.class);
        if (path != null) {
            api.setPath(path.value());
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
    }

    @Override
    public Set<Method> findOperations(Class<?> apiClass) {
        Set<Method> operations = new HashSet<>();
        @SuppressWarnings("unchecked")
        Set<Method> methods = ReflectionUtils.getAllMethods(apiClass);
        for (Method method : methods) {
            if (getHttpMethod(method) != null) {
                operations.add(method);
            }
        }
        return operations;
    }

    private HttpMethod getHttpMethod(Method method) {
        return AnnotationUtil.findAnnotation(method, HttpMethod.class);
    }

}
