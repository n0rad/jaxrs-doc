/**
 *
 *     Copyright (C) norad.fr
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *             http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */
package fr.norad.jaxrs.doc.parser;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import fr.norad.jaxrs.doc.domain.ModelDefinition;
import fr.norad.jaxrs.doc.parserapi.ModelParser;
import fr.norad.jaxrs.doc.utils.AnnotationUtil;

public class ModelJavaParser implements ModelParser {

    private final List<Pattern> modelToIgnore = Arrays.asList(
            Pattern.compile("java\\.[lang|util|io]+\\.[\\w\\._-]+"),
            Pattern.compile("void|int|long|byte|char|short|boolean|float|double"));

    @Override
    public void parse(ModelDefinition model, Class<?> modelClass) {
        model.setModelClass(modelClass);

        Deprecated deprecated = AnnotationUtil.findAnnotation(modelClass, Deprecated.class);
        model.setDeprecated(deprecated != null ? true : null);
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
