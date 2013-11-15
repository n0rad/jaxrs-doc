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

import static fr.norad.jaxrs.doc.utils.TypeUtils.notEmpty;
import java.lang.reflect.Method;
import fr.norad.jaxrs.doc.annotations.Description;
import fr.norad.jaxrs.doc.annotations.Outdated;
import fr.norad.jaxrs.doc.domain.ParameterDefinition;
import fr.norad.jaxrs.doc.parserapi.ParameterParser;
import fr.norad.jaxrs.doc.utils.AnnotationUtil;

public class ParameterJaxrsDocParser implements ParameterParser {

    @Override
    public void parse(ParameterDefinition parameter, Method method, int position) {
        Outdated outdated = AnnotationUtil.findParameterAnnotation(method, position, Outdated.class);
        if (outdated != null) {
            parameter.setDeprecated(true);
            if (notEmpty(outdated.cause())) {
                parameter.setDeprecatedCause(outdated.cause());
            }
            if (notEmpty(outdated.since())) {
                parameter.setDeprecatedSince(outdated.since());
            }
        }

        Description desc = AnnotationUtil.findParameterAnnotation(method, position, Description.class);
        if (desc != null && notEmpty(desc.value())) {
            parameter.setDescription(desc.value());
        }

    }

}
