package net.awired.jaxrs.doc.domain;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@Data
@XmlRootElement
public class ApiDefinition {

    private String path;
    private String description;
    private String summary;
    private Class<?> resourceClass;
    private List<OperationDefinition> operations = new ArrayList<>();

}
