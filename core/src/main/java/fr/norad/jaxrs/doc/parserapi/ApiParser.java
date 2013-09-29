package fr.norad.jaxrs.doc.parserapi;

import java.lang.reflect.Method;
import java.util.Set;
import fr.norad.jaxrs.doc.domain.ApiDefinition;

public interface ApiParser {

    void parse(ApiDefinition api, Class<?> apiClass);

    Set<Method> findOperations(Class<?> apiClass);

}
