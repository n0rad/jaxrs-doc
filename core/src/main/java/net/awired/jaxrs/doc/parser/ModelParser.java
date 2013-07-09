package net.awired.jaxrs.doc.parser;

import java.util.regex.Pattern;
import net.awired.jaxrs.doc.DocConfig;
import net.awired.jaxrs.doc.domain.ProjectDefinition;

public class ModelParser {

    protected final DocConfig config;

    public ModelParser(DocConfig config) {
        this.config = config;
    }

    public void parse(ProjectDefinition project, Class<?> modelClass) {
    }

    protected boolean isIgnoreModel(Class<?> modelClass) {
        for (Pattern pattern : config.getIgnoreModelPatterns()) {
            if (pattern.matcher(modelClass.getName()).matches()) {
                return true;
            }
        }
        return false;
    }

}
