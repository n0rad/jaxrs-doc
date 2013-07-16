package net.awired.jaxrs.doc.parser;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.Path;
import net.awired.jaxrs.doc.DocConfig;
import net.awired.jaxrs.doc.annotations.Description;
import net.awired.jaxrs.doc.annotations.Summary;
import net.awired.jaxrs.doc.domain.ApiDefinition;
import net.awired.jaxrs.doc.domain.OperationDefinition;
import net.awired.jaxrs.doc.domain.ParameterDefinition;
import net.awired.jaxrs.doc.domain.ProjectDefinition;
import net.awired.jaxrs.doc.utils.AnnotationUtil;
import net.awired.jaxrs.doc.utils.ReflectionUtil;

public class OperationParser {

    private final DocConfig config;

    public OperationParser(DocConfig config) {
        this.config = config;
    }

    public OperationDefinition parse(ProjectDefinition project, ApiDefinition api, Method method) {
        OperationDefinition operation = new OperationDefinition();
        HttpMethod httpMethod = getHttpMethod(method);
        if (httpMethod == null) {
            throw new IllegalStateException("parsing method that is not an operation : " + method);
        }
        operation.setHttpMethod(httpMethod.value());
        operation.setMethodName(method.getName());
        operation.setSourceClass(api.getResourceClass());

        Deprecated deprecated = AnnotationUtil.findAnnotation(method, Deprecated.class);
        operation.setDeprecated(deprecated != null ? true : null);

        Summary summary = AnnotationUtil.findAnnotation(method, Summary.class);
        operation.setSummary(summary != null ? summary.value() : null);

        Path path = AnnotationUtil.findAnnotation(method, Path.class);
        String methodPath = path != null ? path.value() : null;
        operation.setPath(buildFullPath(api.getPath(), methodPath));

        Description description = AnnotationUtil.findAnnotation(method, Description.class);
        operation.setDescription(description != null ? description.value() : null);

        fillReturnPart(operation, method);
        config.getModelParser().parse(project, operation.getResponseClass());

        Class<?>[] parameterTypes = method.getParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++) {
            if (operation.getParameters() == null) {
                operation.setParameters(new ArrayList<ParameterDefinition>());
            }
            ParameterDefinition parameter = config.getParameterParser().parse(project, method, i);
            operation.getParameters().add(parameter);
        }

        return operation;
    }

    public boolean isOperation(Method method) {
        return getHttpMethod(method) != null;
    }

    //////////////////////////////////////////////////////////////////

    protected String buildFullPath(String apiPath, String operationPath) {
        if (apiPath.equals("/") && (operationPath == null || operationPath.equals("/"))) {
            return "/";
        }
        StringBuilder builder = new StringBuilder();
        if (!apiPath.startsWith("/")) {
            builder.append('/');
        }
        builder.append(apiPath.endsWith("/") ? apiPath.substring(0, apiPath.length() - 1) : apiPath);
        if (operationPath != null) {
            if (!operationPath.startsWith("/")) {
                builder.append('/');
            }
            builder.append(operationPath.endsWith("/") ? operationPath.substring(0, operationPath.length() - 1) : operationPath);
        }
        return builder.toString();
    }

    protected HttpMethod getHttpMethod(Method method) {
        return AnnotationUtil.findAnnotation(method, HttpMethod.class);
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

        operation.setResponseClass(method.getReturnType());
    }

}
