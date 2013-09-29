package fr.norad.jaxrs.doc.processor;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.TreeSet;
import fr.norad.jaxrs.doc.JaxRsDocProcessorFactory;
import fr.norad.jaxrs.doc.domain.OperationDefinition;
import fr.norad.jaxrs.doc.domain.ParameterDefinition;
import fr.norad.jaxrs.doc.domain.ProjectDefinition;
import fr.norad.jaxrs.doc.parserapi.ParameterParser;

public class ParameterProcessor {

    private final TreeSet<ParameterParser> parsers = new TreeSet<>();
    private final JaxRsDocProcessorFactory factory;

    public ParameterProcessor(JaxRsDocProcessorFactory factory, Collection<ParameterParser> parsers) {
        this.factory = factory;
        this.parsers.addAll(parsers);
    }

    public void process(ProjectDefinition project, OperationDefinition operation, Method method, int position) {
        ParameterDefinition parameter = new ParameterDefinition();

        for (ParameterParser parser : parsers) {
            parser.parse(parameter, method, position);
            factory.getModelProcessor().process(project, parameter.getParamClass());
            factory.getModelProcessor().process(project, parameter.getParamMapKeyClass());
        }

    }

}
