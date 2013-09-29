package fr.norad.jaxrs.doc.processor;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;
import fr.norad.jaxrs.doc.JaxRsDocProcessorFactory;
import fr.norad.jaxrs.doc.domain.ApiDefinition;
import fr.norad.jaxrs.doc.domain.OperationDefinition;
import fr.norad.jaxrs.doc.domain.ProjectDefinition;
import fr.norad.jaxrs.doc.parserapi.OperationParser;

public class OperationProcessor {

    private final Set<OperationParser> operationParsers = new TreeSet<>();
    private final JaxRsDocProcessorFactory factory;

    public OperationProcessor(JaxRsDocProcessorFactory factory, Collection<OperationParser> operationParsers) {
        this.factory = factory;
        this.operationParsers.addAll(operationParsers);
    }

    public void process(ProjectDefinition project, ApiDefinition api, Method method) {
        OperationDefinition operation = new OperationDefinition();
        for (OperationParser operationParser : operationParsers) {
            operationParser.parse(api, operation, method);

            factory.getModelProcessor().process(project, operation.getResponseClass());
            factory.getModelProcessor().process(project, operation.getResponseMapKeyClass());
        }

        Class<?>[] parameterTypes = method.getParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++) {
            factory.getParameterProcessor().process(project, operation, method, i);
        }

        api.getOperations().add(operation);
    }
}
