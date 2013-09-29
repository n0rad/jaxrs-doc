package fr.norad.jaxrs.doc.processor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;
import fr.norad.jaxrs.doc.JaxRsDocProcessorFactory;
import fr.norad.jaxrs.doc.domain.ApiDefinition;
import fr.norad.jaxrs.doc.domain.ProjectDefinition;
import fr.norad.jaxrs.doc.parserapi.ApiParser;

public class ApiProcessor {

    private final Set<ApiParser> apiParsers = new TreeSet<>();
    private final JaxRsDocProcessorFactory factory;

    public ApiProcessor(JaxRsDocProcessorFactory factory, Collection<ApiParser> apiParsers) {
        this.factory = factory;
        apiParsers.addAll(apiParsers);
    }

    public ApiDefinition process(ProjectDefinition project, Class<?> apiClass) {
        ApiDefinition api = new ApiDefinition();
        for (ApiParser parser : apiParsers) {
            parser.parse(api, apiClass);

            Set<Method> operations = parser.findOperations(apiClass);
            for (Method method : operations) {
                factory.getOperationProcessor().process(project, api, method);
            }
        }

        if (project.getApis() == null) {
            project.setApis(new ArrayList<ApiDefinition>());
        }
        return api;
    }
}
