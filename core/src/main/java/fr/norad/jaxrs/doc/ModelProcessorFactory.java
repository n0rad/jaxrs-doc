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

import fr.norad.jaxrs.doc.parser.ModelBeanValidationParser;
import fr.norad.jaxrs.doc.parser.ModelJacksonParser;
import fr.norad.jaxrs.doc.parser.ModelJavaParser;
import fr.norad.jaxrs.doc.parser.ModelJaxrsDocParser;
import fr.norad.jaxrs.doc.parser.ModelJaxrsParser;
import fr.norad.jaxrs.doc.parser.PropertyBeanValidationParser;
import fr.norad.jaxrs.doc.parser.PropertyJavaParser;
import fr.norad.jaxrs.doc.parser.PropertyJaxrsDocParser;
import fr.norad.jaxrs.doc.parserapi.ModelParser;
import fr.norad.jaxrs.doc.parserapi.PropertyParser;
import fr.norad.jaxrs.doc.processor.ModelProcessor;
import fr.norad.jaxrs.doc.processor.PropertyProcessor;
import lombok.Data;

@Data
public class ModelProcessorFactory implements ModelDocFactory {

    private ModelProcessor modelProcessor = new ModelProcessor(this, new ParserHolder<ModelParser>() {
        {
            add(new ModelJavaParser());
            add(new ModelJaxrsParser());
            add(new ModelJaxrsDocParser());
            add(new ModelJacksonParser());
            add(new ModelBeanValidationParser());
        }
    });
    private PropertyProcessor propertyProcessor = new PropertyProcessor(this, new ParserHolder<PropertyParser>() {
        {
            add(new PropertyJavaParser());
            add(new PropertyJaxrsDocParser());
            add(new PropertyBeanValidationParser());
        }
    });

}
