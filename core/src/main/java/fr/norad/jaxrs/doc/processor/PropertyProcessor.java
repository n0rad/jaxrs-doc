package fr.norad.jaxrs.doc.processor;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;
import fr.norad.jaxrs.doc.JaxRsDocProcessorFactory;
import fr.norad.jaxrs.doc.parserapi.OperationParser;
import fr.norad.jaxrs.doc.parserapi.PropertyParser;

public class PropertyProcessor {

    private final Set<OperationParser> parsers = new TreeSet<>();
    private final JaxRsDocProcessorFactory factory;

    public PropertyProcessor(JaxRsDocProcessorFactory factory, Collection<PropertyParser> parsers) {
        this.factory = factory;
        parsers.addAll(parsers);
    }

    public void process() {

    }

}
