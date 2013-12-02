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

import static fr.norad.core.lang.StringUtils.notEmpty;
import java.lang.reflect.Method;
import fr.norad.core.lang.reflect.AnnotationUtils;
import fr.norad.jaxrs.doc.annotations.Description;
import fr.norad.jaxrs.doc.annotations.Outdated;
import fr.norad.jaxrs.doc.annotations.Summary;
import fr.norad.jaxrs.doc.domain.ApiDefinition;
import fr.norad.jaxrs.doc.domain.OperationDefinition;
import fr.norad.jaxrs.doc.parserapi.OperationParser;
import fr.norad.jaxrs.doc.utils.DocUtils;

public class OperationJaxrsDocParser implements OperationParser {

    @Override
    public void parse(ApiDefinition api, OperationDefinition operation, Method method) {

        Summary summary = AnnotationUtils.findAnnotation(method, Summary.class);
        if (summary != null && notEmpty(summary.value())) {
            operation.setSummary(summary.value().trim());
        }

        Outdated outdated = AnnotationUtils.findAnnotation(method, Outdated.class);
        if (outdated != null) {
            operation.setDeprecated(true);
            if (notEmpty(outdated.cause())) {
                operation.setDeprecatedCause(outdated.cause());
            }
            if (notEmpty(outdated.since())) {
                operation.setDeprecatedSince(outdated.since());
            }
            if (notEmpty(outdated.willBeRemovedOn())) {
                operation.setDeprecatedWillBeRemovedOn(outdated.willBeRemovedOn());
            }
        }

        Description desc = AnnotationUtils.findAnnotation(method, Description.class);
        if (desc != null && notEmpty(DocUtils.getDescription(desc))) {
            operation.setDescription(DocUtils.getDescription(desc));
        }
    }

}
