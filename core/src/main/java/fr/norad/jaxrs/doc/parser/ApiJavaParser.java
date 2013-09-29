package fr.norad.jaxrs.doc.parser;

import java.lang.reflect.Method;
import java.util.Set;
import fr.norad.jaxrs.doc.domain.ApiDefinition;
import fr.norad.jaxrs.doc.parserapi.ApiParser;
import fr.norad.jaxrs.doc.utils.AnnotationUtil;

public class ApiJavaParser implements ApiParser {

    @Override
    public void parse(ApiDefinition api, Class<?> apiClass) {
        api.setApiClass(apiClass);

        Deprecated deprecated = AnnotationUtil.findAnnotation(apiClass, Deprecated.class);
        api.setDeprecated(deprecated != null ? true : null);
    }

    @Override
    public Set<Method> findOperations(Class<?> apiClass) {
        return null;
    }

}
