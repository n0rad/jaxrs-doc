package net.awired.jaxrs.doc.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@Data
@XmlRootElement
public class ProjectDefinition {

    private List<ApiDefinition> apis = new ArrayList<>();
    private Map<Class<?>, ModelDefinition> models = new HashMap<>();

    //    var swaggerVersion: String,
    //    var basePath: String,
    //    var resourcePath: String) {
    // apiVersion: "2.20.0-SNAPSHOT"
}
