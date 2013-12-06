package fr.norad.jaxrs.doc.processor;

import fr.norad.jaxrs.doc.JaxrsDocProcessorFactory;
import fr.norad.jaxrs.doc.ParserHolder;
import fr.norad.jaxrs.doc.domain.ErrorDefinition;
import fr.norad.jaxrs.doc.domain.ErrorOperationDefinition;
import fr.norad.jaxrs.doc.domain.ProjectDefinition;
import fr.norad.jaxrs.doc.parserapi.ErrorParser;
import lombok.Getter;

public class ErrorProcessor {

    private final JaxrsDocProcessorFactory factory;
    @Getter
    private ParserHolder<ErrorParser> parsers;

    public ErrorProcessor(JaxrsDocProcessorFactory factory, ParserHolder<ErrorParser> parsers) {
        this.factory = factory;
        this.parsers = parsers;
    }

    public void process(ProjectDefinition project, ErrorOperationDefinition errorOperation) {
        ErrorDefinition error = new ErrorDefinition(errorOperation.getErrorClass());
        project.getErrors().put(error.getErrorClass(), error);
        for (ErrorParser parser : parsers.get()) {
            parser.parse(error, errorOperation.getErrorClass());
        }
    }

}
