package fr.norad.jaxrs.doc.parserapi;

import java.lang.reflect.Method;
import fr.norad.jaxrs.doc.domain.ApiDefinition;
import fr.norad.jaxrs.doc.domain.OperationDefinition;

public interface OperationParser {

    void parse(ApiDefinition api, OperationDefinition operation, Method method);

}
