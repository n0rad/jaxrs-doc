package net.awired.jaxrs.doc.domain;

import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@Data
@XmlRootElement
public class ModelDefinition {

    private String description;
    private Class<?> modelClass;
    private Map<String, PropertyDefinition> properties = new HashMap<>();

    //    private Boolean required = false;
    //    private String name = null;
    //    private String id = "ManagementInfo";
    //    private String allowableValues = null;
    //    private String notes = null;
    //    private String access = null;
    //    private String defaultValue = null;
    //    private String additionalProperties = null;
    //    private String items = null;
    //    private Boolean uniqueItems = false;

}
