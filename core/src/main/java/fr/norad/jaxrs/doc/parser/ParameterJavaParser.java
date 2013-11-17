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
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import fr.norad.jaxrs.doc.domain.LocalizationDefinition;
import fr.norad.jaxrs.doc.domain.ParameterDefinition;
import fr.norad.jaxrs.doc.parserapi.ParameterParser;
import fr.norad.jaxrs.doc.utils.AnnotationUtils;
import fr.norad.jaxrs.doc.utils.ReflectionUtils;

public class ParameterJavaParser implements ParameterParser {

    @Override
    public void parse(Map<Locale, LocalizationDefinition> localeDefinitions, ParameterDefinition parameter,
                      Method method, int position) {
        Deprecated deprecated = AnnotationUtils.findParameterAnnotation(method, position, Deprecated.class);
        if (deprecated != null) {
            parameter.setDeprecated(true);
        }

        fillParamClassPart(parameter, method, position);
    }

    private void fillParamClassPart(ParameterDefinition param, Method method, int position) {
        if (Map.class.isAssignableFrom(method.getParameterTypes()[position])) {
            param.setMapKeyClass(ReflectionUtils.getGenericParamTypeForPosition(method, position, 0));
            param.setParamClass(ReflectionUtils.getGenericParamTypeForPosition(method, position, 1));
            param.setAsList(true);
            return;
        }
        if (Collection.class.isAssignableFrom(method.getParameterTypes()[position])) {
            param.setParamClass(ReflectionUtils.getSingleGenericParamType(method, position));
            param.setAsList(true);
            return;
        }
        if (method.getParameterTypes()[position].isArray()) {
            param.setParamClass(method.getParameterTypes()[position].getComponentType());
            param.setAsList(true);
            return;
        }
        param.setParamClass(method.getParameterTypes()[position]);
    }

}
