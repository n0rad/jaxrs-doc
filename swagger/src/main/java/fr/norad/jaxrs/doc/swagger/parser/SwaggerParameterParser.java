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
import java.util.Locale;
import java.util.Map;
import com.wordnik.swagger.annotations.ApiParam;
import fr.norad.core.lang.reflect.AnnotationUtils;
import fr.norad.jaxrs.doc.api.domain.LocalizationDefinition;
import fr.norad.jaxrs.doc.api.domain.ParameterDefinition;
import fr.norad.jaxrs.doc.parserapi.ParameterParser;

public class SwaggerParameterParser implements ParameterParser {

    @Override
    public void parse(Map<Locale, LocalizationDefinition> localeDefinitions, ParameterDefinition parameter,
                      Method method, int position) {
        ApiParam paramSwagger = AnnotationUtils.findParameterAnnotation(method, position, ApiParam.class);
        if (paramSwagger != null) {
            if (!paramSwagger.value().isEmpty()) {
                parameter.setDescription(paramSwagger.value());
            }
            if (!paramSwagger.name().isEmpty()) {
                parameter.setName(paramSwagger.name());
            }
            if (!paramSwagger.defaultValue().isEmpty()) {
                parameter.setDefaultValue(paramSwagger.defaultValue());
            }
            if (!paramSwagger.allowableValues().isEmpty()) {
                parameter.setAllowedValues(paramSwagger.allowableValues());
            }
            if (paramSwagger.allowMultiple()) {
                parameter.setAsList(true);
            }
            if (paramSwagger.required()) {
                parameter.setMandatory(true);
            }

        }
    }
}
