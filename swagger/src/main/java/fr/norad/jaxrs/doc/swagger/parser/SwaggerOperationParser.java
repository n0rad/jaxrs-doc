package fr.norad.jaxrs.doc.swagger.parser;

import java.lang.reflect.Method;
import fr.norad.jaxrs.doc.DocConfig;
import fr.norad.jaxrs.doc.domain.ApiDefinition;
import fr.norad.jaxrs.doc.domain.OperationDefinition;
import fr.norad.jaxrs.doc.domain.ProjectDefinition;
import fr.norad.jaxrs.doc.parser.OperationParser;

public class SwaggerOperationParser extends OperationParser {

    public SwaggerOperationParser(DocConfig config) {
        super(config);
    }

    @Override
    public OperationDefinition parse(ProjectDefinition project, ApiDefinition api, Method method) {
        OperationDefinition parse = super.parse(project, api, method);

        return parse;
    }

}
