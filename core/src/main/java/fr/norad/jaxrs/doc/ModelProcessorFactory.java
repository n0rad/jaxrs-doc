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
