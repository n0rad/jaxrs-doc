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
import java.util.Arrays;
import java.util.List;
import fr.norad.jaxrs.doc.parser.ApiJavaParser;
import fr.norad.jaxrs.doc.parser.ApiJaxrsDocParser;
import fr.norad.jaxrs.doc.parser.ApiJaxrsParser;
import fr.norad.jaxrs.doc.parser.ModelBeanValidationParser;
import fr.norad.jaxrs.doc.parser.ModelJacksonParser;
import fr.norad.jaxrs.doc.parser.ModelJavaParser;
import fr.norad.jaxrs.doc.parser.ModelJaxrsDocParser;
import fr.norad.jaxrs.doc.parser.ModelJaxrsParser;
import fr.norad.jaxrs.doc.parser.OperationJavaParser;
import fr.norad.jaxrs.doc.parser.OperationJaxrsDocParser;
import fr.norad.jaxrs.doc.parser.OperationJaxrsParser;
import fr.norad.jaxrs.doc.parser.OperationOauth2Parser;
import fr.norad.jaxrs.doc.parser.ParameterBeanValidationParser;
import fr.norad.jaxrs.doc.parser.ParameterCxfParser;
import fr.norad.jaxrs.doc.parser.ParameterJavaParser;
import fr.norad.jaxrs.doc.parser.ParameterJaxrsDocParser;
import fr.norad.jaxrs.doc.parser.ParameterJaxrsParser;
import fr.norad.jaxrs.doc.parser.ParameterJerseyParser;
import fr.norad.jaxrs.doc.parser.ProjectDiscoveryParser;
import fr.norad.jaxrs.doc.parser.PropertyBeanValidationParser;
import fr.norad.jaxrs.doc.parser.PropertyJavaParser;
import fr.norad.jaxrs.doc.parser.PropertyJaxrsDocParser;
import fr.norad.jaxrs.doc.parserapi.ApiParser;
import fr.norad.jaxrs.doc.parserapi.ModelParser;
import fr.norad.jaxrs.doc.parserapi.OperationParser;
import fr.norad.jaxrs.doc.parserapi.ParameterParser;
import fr.norad.jaxrs.doc.parserapi.ProjectParser;
import fr.norad.jaxrs.doc.parserapi.PropertyParser;
import fr.norad.jaxrs.doc.processor.ApiProcessor;
import fr.norad.jaxrs.doc.processor.ModelProcessor;
import fr.norad.jaxrs.doc.processor.OperationProcessor;
import fr.norad.jaxrs.doc.processor.ParameterProcessor;
import fr.norad.jaxrs.doc.processor.ProjectProcessor;
import fr.norad.jaxrs.doc.processor.PropertyProcessor;
import lombok.Data;

@Data
public class JaxrsDocProcessorFactory {

    private ProjectProcessor projectProcessor;
    private ApiProcessor apiProcessor = new ApiProcessor(this, new ArrayList<ApiParser>() {
        {
            add(new ApiJavaParser());
            add(new ApiJaxrsParser());
            add(new ApiJaxrsDocParser());
        }
    });
    private OperationProcessor operationProcessor = new OperationProcessor(this, new ArrayList<OperationParser>() {
        {
            add(new OperationJavaParser());
            add(new OperationJaxrsParser());
            add(new OperationJaxrsDocParser());
            add(new OperationOauth2Parser());
        }
    });
    private ParameterProcessor parameterProcessor = new ParameterProcessor(this, new ArrayList<ParameterParser>() {
        {
            add(new ParameterJavaParser());
            add(new ParameterJaxrsParser());
            add(new ParameterJerseyParser());
            add(new ParameterCxfParser());
            add(new ParameterJaxrsDocParser());
            add(new ParameterBeanValidationParser());
        }
    });
    private ModelProcessor modelProcessor = new ModelProcessor(this, new ArrayList<ModelParser>() {
        {
            add(new ModelJavaParser());
            add(new ModelJaxrsParser());
            add(new ModelJaxrsDocParser());
            add(new ModelJacksonParser());
            add(new ModelBeanValidationParser());
        }
    });
    private PropertyProcessor propertyProcessor = new PropertyProcessor(this, new ArrayList<PropertyParser>() {
        {
            add(new PropertyJavaParser());
            add(new PropertyJaxrsDocParser());
            add(new PropertyBeanValidationParser());
        }
    });

    public JaxrsDocProcessorFactory(ProjectParser projectParser) {
        projectProcessor = new ProjectProcessor(this, Arrays.<ProjectParser> asList(projectParser));
    }

    public JaxrsDocProcessorFactory(List<String> packagesToScan, String name, String version) {
        projectProcessor = new ProjectProcessor(this, Arrays.<ProjectParser> asList(new ProjectDiscoveryParser(
                packagesToScan, name, version)));
    }

}
