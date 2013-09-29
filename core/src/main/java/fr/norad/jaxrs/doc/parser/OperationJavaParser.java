package fr.norad.jaxrs.doc.parser;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import fr.norad.jaxrs.doc.domain.ApiDefinition;
import fr.norad.jaxrs.doc.domain.ErrorDefinition;
import fr.norad.jaxrs.doc.domain.OperationDefinition;
import fr.norad.jaxrs.doc.parserapi.OperationParser;
import fr.norad.jaxrs.doc.utils.AnnotationUtil;
import fr.norad.jaxrs.doc.utils.ReflectionUtil;

public class OperationJavaParser implements OperationParser {

    @Override
    public void parse(ApiDefinition api, OperationDefinition operation, Method method) {
        operation.setMethodName(method.getName());
        operation.setSourceClass(api.getApiClass());

        Deprecated deprecated = AnnotationUtil.findAnnotation(method, Deprecated.class);
        operation.setDeprecated(deprecated != null ? true : null);

        List<Class<?>> exceptions = ReflectionUtil.getExceptions(method);
        for (Class<?> exception : exceptions) {
            if (operation.getErrors() == null) {
                operation.setErrors(new ArrayList<ErrorDefinition>());
            }
            operation.getErrors().add(new ErrorDefinition(exception));
        }

        fillReturnPart(operation, method);
    }

    protected void fillReturnPart(OperationDefinition operation, Method method) {
        if (Map.class.isAssignableFrom(method.getReturnType())) {
            operation.setResponseMapKeyClass(ReflectionUtil.getGenericReturnTypeForPosition(method, 0));
            operation.setResponseClass(ReflectionUtil.getGenericReturnTypeForPosition(method, 1));
            operation.setResponseAsList(true);
            return;
        }
        if (Collection.class.isAssignableFrom(method.getReturnType())) {
            operation.setResponseClass(ReflectionUtil.getSingleGenericReturnType(method));
            operation.setResponseAsList(true);
            return;
        }
        if (method.getReturnType().isArray()) {
            operation.setResponseClass(method.getReturnType().getComponentType());
            operation.setResponseAsList(true);
            return;
        }

        if (!method.getReturnType().equals(Void.TYPE)) {
            operation.setResponseClass(method.getReturnType());
        }
    }

}
