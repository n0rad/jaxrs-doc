package fr.norad.jaxrs.doc.parserapi;

import java.lang.reflect.Method;
import fr.norad.jaxrs.doc.domain.ParameterDefinition;

public interface ParameterParser {

    void parse(ParameterDefinition parameter, Method method, int position);

}
