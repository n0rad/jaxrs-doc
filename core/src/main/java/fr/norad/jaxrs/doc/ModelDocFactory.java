package fr.norad.jaxrs.doc;


import fr.norad.jaxrs.doc.processor.ModelProcessor;
import fr.norad.jaxrs.doc.processor.PropertyProcessor;

public interface ModelDocFactory {

    ModelProcessor getModelProcessor();

    PropertyProcessor getPropertyProcessor();

}
