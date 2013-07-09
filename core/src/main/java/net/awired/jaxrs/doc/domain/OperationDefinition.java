package net.awired.jaxrs.doc.domain;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import net.awired.jaxrs.doc.domain.sub.VersionDefinition;

@Data
@XmlRootElement
public class OperationDefinition {

    private String httpMethod;
    private String description;
    private String methodName; // method name
    private Boolean deprecated;
    private String path;
    private Class<?> responseClass;
    private Class<?> responseMapKeyClass;
    private Boolean responseAsList;
    private String summary;

    private String notes;
    private VersionDefinition version;
    private String types;
    private String tags;
    private List<ParameterDefinition> parameters;

}
