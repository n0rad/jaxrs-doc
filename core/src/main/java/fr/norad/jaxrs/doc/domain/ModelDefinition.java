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
package fr.norad.jaxrs.doc.domain;

import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@Data
@XmlRootElement
public class ModelDefinition {

    private String description;
    private final Class<?> modelClass;
    private Map<String, PropertyDefinition> properties = new HashMap<>();
    private Map<String, Object> extras;

    private Boolean deprecated;
    private String deprecatedSince;
    private String deprecatedCause;

    public ModelDefinition(Class<?> modelClass) {
        this.modelClass = modelClass;
    }

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
