package fr.norad.jaxrs.doc.parserapi;

import fr.norad.jaxrs.doc.domain.ModelDefinition;

public interface ModelParser {

    void parse(ModelDefinition model, Class<?> modelClass);

    boolean isModelToIgnore(Class<?> modelClass);

}
