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
import fr.norad.jaxrs.doc.PropertyAccessor;
import fr.norad.jaxrs.doc.annotations.Description;
import fr.norad.jaxrs.doc.annotations.Outdated;
import fr.norad.jaxrs.doc.domain.PropertyDefinition;
import fr.norad.jaxrs.doc.parserapi.PropertyParser;
import fr.norad.jaxrs.doc.utils.AnnotationUtil;

public class PropertyJaxrsDocParser implements PropertyParser {

    @Override
    public void parse(PropertyDefinition property, PropertyAccessor accessor) {
        if (accessor.getField() != null) {
            Outdated outdated = accessor.getField().getAnnotation(Outdated.class);
            if (outdated != null) {
                property.setDeprecated(true);

                if (notEmpty(outdated.cause())) {
                    property.setDeprecatedCause(outdated.cause());
                }
                if (notEmpty(outdated.since())) {
                    property.setDeprecatedSince(outdated.since());
                }
            }

            Description desc = accessor.getField().getAnnotation(Description.class);
            if (desc != null && notEmpty(desc.value())) {
                property.setDescription(desc.value());
            }
        }

        processGetterSetter(property, accessor.getGetter());
        processGetterSetter(property, accessor.getSetter());
    }

    private void processGetterSetter(PropertyDefinition property, Method method) {
        if (method == null) {
            return;
        }

        Description desc = AnnotationUtil.findAnnotation(method, Description.class);
        if (desc != null && notEmpty(desc.value())) {
            property.setDescription(desc.value());
        }

        Outdated outdated = AnnotationUtil.findAnnotation(method, Outdated.class);
        if (outdated != null) {
            property.setDeprecated(true);
            if (notEmpty(outdated.cause())) {
                property.setDeprecatedCause(outdated.cause());
            }
            if (notEmpty(outdated.since())) {
                property.setDeprecatedSince(outdated.since());
            }
        }

    }

    @Override
    public void isPropertyToIgnore(PropertyAccessor accessor) {
        // TODO Auto-generated method stub

    }

}
