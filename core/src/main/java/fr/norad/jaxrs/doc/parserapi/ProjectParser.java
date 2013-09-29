package fr.norad.jaxrs.doc.parserapi;

import java.util.List;
import fr.norad.jaxrs.doc.domain.ProjectDefinition;

public interface ProjectParser {

    void parse(ProjectDefinition project);

    List<Class<?>> apiClasses();

}
