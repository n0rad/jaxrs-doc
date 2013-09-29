package fr.norad.jaxrs.doc.processor;

import java.util.Collection;
import java.util.List;
import java.util.TreeSet;
import fr.norad.jaxrs.doc.JaxRsDocProcessorFactory;
import fr.norad.jaxrs.doc.domain.ApiDefinition;
import fr.norad.jaxrs.doc.domain.ProjectDefinition;
import fr.norad.jaxrs.doc.parserapi.ProjectParser;

public class ProjectProcessor {

    private final TreeSet<ProjectParser> parsers = new TreeSet<>();
    private final JaxRsDocProcessorFactory factory;

    public ProjectProcessor(JaxRsDocProcessorFactory factory, Collection<ProjectParser> parsers) {
        this.factory = factory;
        this.parsers.addAll(parsers);
    }

    public ProjectDefinition process() {
        ProjectDefinition project = new ProjectDefinition();
        for (ProjectParser parser : parsers) {
            parser.parse(project);
            List<Class<?>> apiClasses = parser.apiClasses();
            for (Class<?> apiClass : apiClasses) {
                ApiDefinition api = factory.getApiProcessor().process(project, apiClass);
                project.getApis().add(api);
            }
        }
        return project;
    }

}
