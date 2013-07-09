package net.awired.jaxrs.doc.domain;

import javax.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import net.awired.jaxrs.doc.domain.sub.ParameterType;

@Data
@XmlRootElement
public class ParameterDefinition {

    private ParameterType paramType;
    private Boolean encoded;
    private String paramName;
    private Class<?> paramClass;
    private Boolean paramAsList;
    private Class<?> paramMapKeyClass;
    private String defaultValue;
    private String description;

    //    private String name;
    //    private String description;
    //    private String notes;
    //    private String allowableValues;
    //    private Boolean required;
    //    private Boolean allowMultiple;
    //    private String paramAccess;
    //    private String internalDescription;
    //    private String wrapperName;
    //    private String dataType ;

}
