package fr.norad.jaxrs.doc.parserapi;

import fr.norad.jaxrs.doc.domain.ErrorDefinition;
import fr.norad.jaxrs.doc.domain.ProjectDefinition;

public interface ErrorParser {

    public void parse(ErrorDefinition error, Class<? extends Exception> exception);
}
