package fr.norad.jaxrs.doc.parser;

import static fr.norad.core.lang.StringUtils.notEmpty;
import fr.norad.core.lang.reflect.AnnotationUtils;
import fr.norad.jaxrs.doc.annotations.Description;
import fr.norad.jaxrs.doc.annotations.ErrorType;
import fr.norad.jaxrs.doc.annotations.HttpStatus;
import fr.norad.jaxrs.doc.domain.ErrorDefinition;
import fr.norad.jaxrs.doc.parserapi.ErrorParser;
import fr.norad.jaxrs.doc.utils.DocUtils;

public class ErrorJaxrsDocParser implements ErrorParser {

    @Override
    public void parse(ErrorDefinition error, Class<? extends Exception> e) {
        HttpStatus status = AnnotationUtils.findAnnotation(e, HttpStatus.class);
        if (status != null) {
            error.setHttpStatus(status.value());
        }

        Description desc = AnnotationUtils.findAnnotation(e, Description.class);
        if (desc != null && notEmpty(DocUtils.getDescription(desc))) {
            error.setDescription(DocUtils.getDescription(desc));
        }

        ErrorType type = AnnotationUtils.findAnnotation(e, ErrorType.class);
        if (type != null) {
            error.setType(type.value());
        }
    }
}
