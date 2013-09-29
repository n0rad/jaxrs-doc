package fr.norad.jaxrs.doc.parser;

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
            parameter.setDeprecatedCause(outdated.cause());
            parameter.setDeprecatedSince(outdated.since().isEmpty() ? null : outdated.since());
        }

        Description desc = AnnotationUtil.findParameterAnnotation(method, position, Description.class);
        parameter.setDescription(desc == null ? null : desc.value());

    }

}
