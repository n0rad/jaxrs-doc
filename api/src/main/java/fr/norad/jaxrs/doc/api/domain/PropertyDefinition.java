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
package fr.norad.jaxrs.doc.api.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@Data
@XmlRootElement
public class PropertyDefinition {

    private Boolean asList;
    private Class<?> mapKeyClass;
    private Class<?> propertyClass;
    private String description;
    private Boolean validationCascaded;
    private List<ConstraintDefinition> constraints;
    private Object defaultValue;

    private Boolean deprecated;
    private String deprecatedSince;
    private String deprecatedCause;
    private String deprecatedWillBeRemovedOn;

    private Map<String, Object> extras;

    public Map<String, Object> createdExtras() {
        if (extras == null) {
            extras = new HashMap<>();
        }
        return extras;
    }

}
