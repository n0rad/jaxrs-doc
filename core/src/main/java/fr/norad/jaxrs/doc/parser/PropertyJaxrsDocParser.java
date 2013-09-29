package fr.norad.jaxrs.doc.parser;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import fr.norad.jaxrs.doc.annotations.Description;
import fr.norad.jaxrs.doc.annotations.Outdated;
import fr.norad.jaxrs.doc.domain.PropertyDefinition;
import fr.norad.jaxrs.doc.parserapi.PropertyParser;
import fr.norad.jaxrs.doc.utils.AnnotationUtil;

public class PropertyJaxrsDocParser implements PropertyParser {

    @Override
    public void parse(PropertyDefinition property, Field field, Method getter, Method setter) {
        if (field != null) {
            Outdated outdated = field.getAnnotation(Outdated.class);
            if (outdated != null) {
                property.setDeprecated(true);
                property.setDeprecatedCause(outdated.cause());
                property.setDeprecatedSince(outdated.since().isEmpty() ? null : outdated.since());
            }

            Description desc = field.getAnnotation(Description.class);
            if (property.getDescription() == null) {
                property.setDescription(desc == null ? null : desc.value());
            }
        }

        processGetterSetter(property, getter);
        processGetterSetter(property, setter);
    }

    private void processGetterSetter(PropertyDefinition property, Method method) {
        if (method == null) {
            return;
        }

        Description desc = AnnotationUtil.findAnnotation(method, Description.class);
        if (property.getDescription() == null) {
            property.setDescription(desc == null ? null : desc.value());
        }

        Outdated outdated = AnnotationUtil.findAnnotation(method, Outdated.class);
        if (outdated != null) {
            property.setDeprecated(true);
            property.setDeprecatedCause(outdated.cause());
            property.setDeprecatedSince(outdated.since().isEmpty() ? null : outdated.since());
        }

    }

}
