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
package fr.norad.jaxrs.doc;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import lombok.Data;
import fr.norad.jaxrs.doc.parser.ModelJacksonParser;
import fr.norad.jaxrs.doc.parser.jaxrs.JaxrsApiParser;
import fr.norad.jaxrs.doc.parser.jaxrs.ModelParser;
import fr.norad.jaxrs.doc.parser.jaxrs.OperationParser;
import fr.norad.jaxrs.doc.parser.jaxrs.ParameterParser;
import fr.norad.jaxrs.doc.parser.jaxrs.ProjectParser;
import fr.norad.jaxrs.doc.parser.jaxrs.PropertyParser;

@Data
public class DocConfig {

    private final List<Pattern> ignoreModelPatterns = new ArrayList<>();

    // parsers
    private ProjectParser projectParser = new ProjectParser(this);
    private JaxrsApiParser apiParser = new JaxrsApiParser(this);
    private OperationParser operationParser = new OperationParser(this);
    private ParameterParser parameterParser = new ParameterParser(this);
    private ModelParser modelParser = new ModelJacksonParser(this);
    private PropertyParser propertyParser = new PropertyParser(this);

    // project
    private List<String> projectPackagesToScan;

    //project.type   scan class or interface or both ?? 

    public DocConfig(List<String> packageToScan) {
        projectPackagesToScan = packageToScan;
        ignoreModelPatterns.add(Pattern.compile("java\\.[lang|util|io]+\\.[\\w\\._-]+"));
        ignoreModelPatterns.add(Pattern.compile("void|int|long|byte|char|short|boolean|float|double"));
        ignoreModelPatterns.add(Pattern.compile("javax\\.ws\\.rs\\.core\\.Response"));
    }

    public void addIgnoreModelPattern(Pattern pattern) {
        ignoreModelPatterns.add(pattern);
    }

}
