package fr.norad.jaxrs.doc.parser;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import fr.norad.jaxrs.doc.domain.ModelDefinition;
import fr.norad.jaxrs.doc.parserapi.ModelParser;

public class ModelJaxrsParser implements ModelParser {

    private final List<Pattern> modelToIgnore = Arrays.asList(Pattern.compile("javax\\.ws\\.rs\\.core\\.Response"));

    @Override
    public void parse(ModelDefinition model, Class<?> modelClass) {
        // for now I don't see any jaxrs information to fill from model
    }

    @Override
    public boolean isModelToIgnore(Class<?> modelClass) {
        for (Pattern pattern : modelToIgnore) {
            if (pattern.matcher(modelClass.getName()).matches()) {
                return true;
            }
        }
        return false;
    }

}
