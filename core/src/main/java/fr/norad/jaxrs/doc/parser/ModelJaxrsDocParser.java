package fr.norad.jaxrs.doc.parser;

import fr.norad.jaxrs.doc.annotations.Description;
import fr.norad.jaxrs.doc.annotations.Outdated;
import fr.norad.jaxrs.doc.domain.ModelDefinition;
import fr.norad.jaxrs.doc.parserapi.ModelParser;
import fr.norad.jaxrs.doc.utils.AnnotationUtil;

public class ModelJaxrsDocParser implements ModelParser {

    @Override
    public void parse(ModelDefinition model, Class<?> modelClass) {
        Outdated outdated = AnnotationUtil.findAnnotation(modelClass, Outdated.class);
        if (outdated != null) {
            model.setDeprecated(true);
            model.setDeprecatedCause(outdated.cause());
            model.setDeprecatedSince(outdated.since().isEmpty() ? null : outdated.since());
        }

        Description description = AnnotationUtil.findAnnotation(modelClass, Description.class);
        model.setDescription(description != null ? description.value() : null);

    }

    @Override
    public boolean isModelToIgnore(Class<?> modelClass) {
        return false;
    }

}
