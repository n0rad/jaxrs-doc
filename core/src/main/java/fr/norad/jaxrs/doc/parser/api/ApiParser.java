package fr.norad.jaxrs.doc.parser.api;

import fr.norad.jaxrs.doc.domain.ApiDefinition;
import fr.norad.jaxrs.doc.domain.ProjectDefinition;

public interface ApiParser {

    public ApiDefinition parse(ProjectDefinition project, Class<?> apiClass);
}
