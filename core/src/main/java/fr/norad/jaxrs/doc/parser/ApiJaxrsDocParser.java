package fr.norad.jaxrs.doc.parser;

import java.lang.reflect.Method;
import java.util.Set;
import fr.norad.jaxrs.doc.annotations.Outdated;
import fr.norad.jaxrs.doc.domain.ApiDefinition;
import fr.norad.jaxrs.doc.parserapi.ApiParser;
import fr.norad.jaxrs.doc.utils.AnnotationUtil;

public class ApiJaxrsDocParser implements ApiParser {

    @Override
    public void parse(ApiDefinition api, Class<?> apiClass) {
        Outdated outdated = AnnotationUtil.findAnnotation(apiClass, Outdated.class);
        if (outdated != null) {
            api.setDeprecated(true);
            api.setDeprecatedCause(outdated.cause());
            api.setDeprecatedSince(outdated.since().isEmpty() ? null : outdated.since());
        }
    }

    @Override
    public Set<Method> findOperations(Class<?> apiClass) {
        return null;
    }

}
