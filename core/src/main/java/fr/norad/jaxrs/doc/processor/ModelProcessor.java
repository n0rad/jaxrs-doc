package fr.norad.jaxrs.doc.processor;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;
import fr.norad.jaxrs.doc.JaxRsDocProcessorFactory;
import fr.norad.jaxrs.doc.domain.ProjectDefinition;
import fr.norad.jaxrs.doc.parserapi.ModelParser;

public class ModelProcessor {

    private final Set<ModelParser> parsers = new TreeSet<>();
    private final JaxRsDocProcessorFactory factory;

    public ModelProcessor(JaxRsDocProcessorFactory factory, Collection<ModelParser> modelParsers) {
        this.factory = factory;
        modelParsers.addAll(modelParsers);
    }

    public void process(ProjectDefinition project, Class<?> modelClass) {

        project.getModels().put(model.getModelClass().getName(), model);

    }

}
