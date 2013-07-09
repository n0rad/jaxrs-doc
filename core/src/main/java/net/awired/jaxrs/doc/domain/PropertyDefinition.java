package net.awired.jaxrs.doc.domain;

import javax.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@Data
@XmlRootElement
public class PropertyDefinition {

    private Boolean asList;
    private Class<?> propertyClass;
    private String description;

}
