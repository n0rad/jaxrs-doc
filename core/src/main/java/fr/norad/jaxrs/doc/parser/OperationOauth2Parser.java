package fr.norad.jaxrs.doc.parser;

import java.lang.reflect.Method;
import fr.norad.jaxrs.doc.domain.ApiDefinition;
import fr.norad.jaxrs.doc.domain.OperationDefinition;
import fr.norad.jaxrs.doc.parserapi.OperationParser;
import fr.norad.jaxrs.oauth2.SecuredUtils;

public class OperationOauth2Parser implements OperationParser {
    @Override
    public void parse(ApiDefinition api, OperationDefinition operation, Method method) {
        operation.setSecured(SecuredUtils.findSecured(method));
    }
}
