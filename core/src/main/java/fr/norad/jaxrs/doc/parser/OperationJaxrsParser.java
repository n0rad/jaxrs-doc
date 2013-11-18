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
import javax.ws.rs.Consumes;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import fr.norad.core.lang.reflect.AnnotationUtils;
import fr.norad.jaxrs.doc.domain.ApiDefinition;
import fr.norad.jaxrs.doc.domain.OperationDefinition;
import fr.norad.jaxrs.doc.parserapi.OperationParser;

public class OperationJaxrsParser implements OperationParser {

    @Override
    public void parse(ApiDefinition api, OperationDefinition operation, Method method) {
        HttpMethod httpMethod = getHttpMethod(method);
        if (httpMethod != null) {
            operation.setHttpMethod(httpMethod.value());
        }

        Path path = AnnotationUtils.findAnnotation(method, Path.class);
        String methodPath = null;
        if (path != null) {
            methodPath = path.value();
        }
        operation.setPath(buildFullPath(api.getPath(), methodPath));

        Consumes consumes = AnnotationUtils.findAnnotation(method, Consumes.class);
        if (consumes != null) {
            for (String consume : consumes.value()) {
                if (operation.getConsumes() == null) {
                    operation.setConsumes(new ArrayList<String>());
                }
                operation.getConsumes().add(consume);
            }
        }

        Produces produces = AnnotationUtils.findAnnotation(method, Produces.class);
        if (produces != null) {
            for (String produce : produces.value()) {
                if (operation.getProduces() == null) {
                    operation.setProduces(new ArrayList<String>());
                }
                operation.getProduces().add(produce);
            }
        }

    }

    //////////////////////////////////////////////////////////////////

    private String buildFullPath(String apiPath, String operationPath) {
        if (apiPath.equals("/") && (operationPath == null || operationPath.equals("/"))) {
            return "/";
        }
        StringBuilder builder = new StringBuilder();
        if (!apiPath.startsWith("/")) {
            builder.append('/');
        }
        builder.append(apiPath.endsWith("/") ? apiPath.substring(0, apiPath.length() - 1) : apiPath);
        if (operationPath != null) {
            if (!operationPath.startsWith("/")) {
                builder.append('/');
            }
            builder.append(operationPath.endsWith("/") ? operationPath.substring(0, operationPath.length() - 1) : operationPath);
        }
        return builder.toString();
    }

    private HttpMethod getHttpMethod(Method method) {
        return AnnotationUtils.findAnnotation(method, HttpMethod.class);
    }

}
