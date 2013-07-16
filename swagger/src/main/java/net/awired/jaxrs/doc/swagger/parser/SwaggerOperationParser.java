package net.awired.jaxrs.doc.swagger.parser;

import java.lang.reflect.Method;
import net.awired.jaxrs.doc.DocConfig;
import net.awired.jaxrs.doc.domain.ApiDefinition;
import net.awired.jaxrs.doc.domain.OperationDefinition;
import net.awired.jaxrs.doc.domain.ProjectDefinition;
import net.awired.jaxrs.doc.parser.OperationParser;

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
