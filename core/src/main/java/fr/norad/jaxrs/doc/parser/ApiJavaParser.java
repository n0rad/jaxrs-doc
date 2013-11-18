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
import java.util.Set;
import fr.norad.core.lang.reflect.AnnotationUtils;
import fr.norad.jaxrs.doc.domain.ApiDefinition;
import fr.norad.jaxrs.doc.parserapi.ApiParser;

public class ApiJavaParser implements ApiParser {

    @Override
    public void parse(ApiDefinition api, Class<?> apiClass) {
        api.setApiClass(apiClass);

        Deprecated deprecated = AnnotationUtils.findAnnotation(apiClass, Deprecated.class);
        api.setDeprecated(deprecated != null ? true : null);
    }

    @Override
    public Set<Method> findOperations(Class<?> apiClass) {
        return null;
    }

}
