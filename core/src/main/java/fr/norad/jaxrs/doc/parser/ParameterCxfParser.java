package fr.norad.jaxrs.doc.parser;

import java.lang.reflect.Method;
import java.util.Locale;
import java.util.Map;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import fr.norad.core.lang.reflect.AnnotationUtils;
import fr.norad.jaxrs.doc.domain.LocalizationDefinition;
import fr.norad.jaxrs.doc.domain.ParameterDefinition;
import fr.norad.jaxrs.doc.domain.ParameterType;
import fr.norad.jaxrs.doc.parserapi.ParameterParser;

public class ParameterCxfParser implements ParameterParser {

    @Override
    public void parse(Map<Locale, LocalizationDefinition> localeDefinitions, ParameterDefinition parameter,
                      Method method, int position) {
        Multipart formData = AnnotationUtils.findParameterAnnotation(method, position, Multipart.class);
        if (formData != null) {
            parameter.setName(formData.value());
            parameter.setType(ParameterType.MULTIPART);
            return;
        }
    }

}
