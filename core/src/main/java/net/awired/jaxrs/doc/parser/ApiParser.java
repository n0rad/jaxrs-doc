package net.awired.jaxrs.doc.parser;

import java.lang.reflect.Method;
import java.util.Set;
import javax.ws.rs.Path;
import net.awired.jaxrs.doc.DocConfig;
import net.awired.jaxrs.doc.annotations.Description;
import net.awired.jaxrs.doc.annotations.Summary;
import net.awired.jaxrs.doc.domain.ApiDefinition;
import net.awired.jaxrs.doc.domain.ProjectDefinition;
import net.awired.jaxrs.doc.utils.AnnotationUtil;
import org.reflections.ReflectionUtils;

public class ApiParser {

    private final DocConfig config;

    public ApiParser(DocConfig docConfig) {
        this.config = docConfig;
    }

    public ApiDefinition parse(ProjectDefinition project, Class<?> apiClass) {
        ApiDefinition api = new ApiDefinition();
        api.setResourceClass(apiClass);

        Path annotation = AnnotationUtil.findAnnotation(apiClass, Path.class);
        api.setPath(annotation.value());

        Description description = AnnotationUtil.findAnnotation(apiClass, Description.class);
        api.setDescription(description != null ? description.value() : null);

        Summary summary = AnnotationUtil.findAnnotation(apiClass, Summary.class);
        api.setSummary(summary != null ? summary.value() : null);

        @SuppressWarnings("unchecked")
        Set<Method> methods = ReflectionUtils.getAllMethods(apiClass);
        for (Method method : methods) {
            if (config.getOperationParser().isOperation(method)) {
                api.getOperations().add(config.getOperationParser().parse(project, method));
            }
        }

        return api;
    }

}
