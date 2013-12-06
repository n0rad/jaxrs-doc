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
import fr.norad.core.lang.reflect.AnnotationUtils;
import fr.norad.jaxrs.doc.api.Description;
import fr.norad.jaxrs.doc.api.ErrorType;
import fr.norad.jaxrs.doc.api.HttpStatus;
import fr.norad.jaxrs.doc.domain.ErrorDefinition;
import fr.norad.jaxrs.doc.parserapi.ErrorParser;
import fr.norad.jaxrs.doc.utils.DocUtils;

public class ErrorJaxrsDocParser implements ErrorParser {

    @Override
    public void parse(ErrorDefinition error, Class<? extends Exception> e) {
        HttpStatus status = AnnotationUtils.findAnnotation(e, HttpStatus.class);
        if (status != null) {
            error.setHttpStatus(status.value());
        }

        Description desc = AnnotationUtils.findAnnotation(e, Description.class);
        if (desc != null && notEmpty(DocUtils.getDescription(desc))) {
            error.setDescription(DocUtils.getDescription(desc));
        }

        ErrorType type = AnnotationUtils.findAnnotation(e, ErrorType.class);
        if (type != null) {
            error.setType(type.value());
        }
    }
}
