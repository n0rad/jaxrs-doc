package fr.norad.jaxrs.doc.parserapi;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import fr.norad.jaxrs.doc.domain.PropertyDefinition;

public interface PropertyParser {

    void parse(PropertyDefinition property, Field field, Method getter, Method setter);

}
