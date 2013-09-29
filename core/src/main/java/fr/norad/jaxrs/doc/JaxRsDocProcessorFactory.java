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

import static java.util.Arrays.asList;
import java.util.Arrays;
import java.util.List;
import lombok.Data;
import fr.norad.jaxrs.doc.parser.ApiJavaParser;
import fr.norad.jaxrs.doc.parser.ApiJaxrsDocParser;
import fr.norad.jaxrs.doc.parser.ApiJaxrsParser;
import fr.norad.jaxrs.doc.parser.ModelJavaParser;
import fr.norad.jaxrs.doc.parser.ModelJaxrsDocParser;
import fr.norad.jaxrs.doc.parser.ModelJaxrsParser;
import fr.norad.jaxrs.doc.parser.OperationJavaParser;
import fr.norad.jaxrs.doc.parser.OperationJaxrsDocParser;
import fr.norad.jaxrs.doc.parser.OperationJaxrsParser;
import fr.norad.jaxrs.doc.parser.ParameterJavaParser;
import fr.norad.jaxrs.doc.parser.ParameterJaxrsDocParser;
import fr.norad.jaxrs.doc.parser.ParameterJaxrsParser;
import fr.norad.jaxrs.doc.parser.ProjectDiscoveryParser;
import fr.norad.jaxrs.doc.parser.PropertyJavaParser;
import fr.norad.jaxrs.doc.parser.PropertyJaxrsDocParser;
import fr.norad.jaxrs.doc.parserapi.ProjectParser;
import fr.norad.jaxrs.doc.processor.ApiProcessor;
import fr.norad.jaxrs.doc.processor.ModelProcessor;
import fr.norad.jaxrs.doc.processor.OperationProcessor;
import fr.norad.jaxrs.doc.processor.ParameterProcessor;
import fr.norad.jaxrs.doc.processor.ProjectProcessor;
import fr.norad.jaxrs.doc.processor.PropertyProcessor;

@Data
public class JaxRsDocProcessorFactory {

    private ProjectProcessor projectProcessor;
    private ApiProcessor apiProcessor = new ApiProcessor(this, asList( //
            new ApiJavaParser(), //
            new ApiJaxrsParser(), //
            new ApiJaxrsDocParser()));
    private OperationProcessor operationProcessor = new OperationProcessor(this, asList( //
            new OperationJavaParser(), //
            new OperationJaxrsParser(), //
            new OperationJaxrsDocParser()));
    private ParameterProcessor parameterProcessor = new ParameterProcessor(this, asList( //
            new ParameterJavaParser(), //
            new ParameterJaxrsParser(), //
            new ParameterJaxrsDocParser()));
    private ModelProcessor modelProcessor = new ModelProcessor(this, asList( //
            new ModelJavaParser(), //
            new ModelJaxrsParser(), //
            new ModelJaxrsDocParser() //
            ));
    private PropertyProcessor propertyProcessor = new PropertyProcessor(this, asList( //
            new PropertyJavaParser(), //
            new PropertyJaxrsDocParser() //
            ));

    public JaxRsDocProcessorFactory(ProjectParser projectParser) {
        projectProcessor = new ProjectProcessor(this, Arrays.<ProjectParser> asList(projectParser));
    }

    public JaxRsDocProcessorFactory(List<String> packagesToScan, String name, String version) {
        projectProcessor = new ProjectProcessor(this, Arrays.<ProjectParser> asList(new ProjectDiscoveryParser(
                packagesToScan, name, version)));
    }

}
