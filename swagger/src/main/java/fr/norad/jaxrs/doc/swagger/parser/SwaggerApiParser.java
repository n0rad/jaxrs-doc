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
package fr.norad.jaxrs.doc.swagger.parser;

import java.lang.reflect.Method;
import java.util.Set;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiClass;
import fr.norad.core.lang.reflect.AnnotationUtils;
import fr.norad.jaxrs.doc.domain.ApiDefinition;
import fr.norad.jaxrs.doc.parserapi.ApiParser;

public class SwaggerApiParser implements ApiParser {

    @Override
    public void parse(ApiDefinition api, Class<?> apiClass) {
        Api apiSwagger = AnnotationUtils.findAnnotation(apiClass, Api.class);
        if (apiSwagger != null) {
            if (apiSwagger.value() != null) {
                api.setPath(apiSwagger.value());
            }
            if (apiSwagger.description() != null) {
                api.setDescription(apiSwagger.description());
            }
        }

        ApiClass apiClassSwagger = AnnotationUtils.findAnnotation(apiClass, ApiClass.class);
        if (apiClassSwagger != null) {
            if (apiClassSwagger.description() != null) {
                api.setDescription(apiClassSwagger.description());
            }
            if (apiClassSwagger.value() != null) {
                api.setSummary(apiClassSwagger.value());
            }
        }
    }

    @Override
    public Set<Method> findOperations(Class<?> apiClass) {
        return null;
    }

}
