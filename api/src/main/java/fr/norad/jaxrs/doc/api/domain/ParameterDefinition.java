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

import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@Data
@XmlRootElement
public class ParameterDefinition {

    private ParameterType type;
    private Boolean encoded;
    private String name;
    private Class<?> paramClass;
    private Boolean asList;
    private Class<?> mapKeyClass;
    private String defaultValue;
    private String allowedValues; //TODO process enum
    private String description;
    private Map<String, Object> extras;
    private Boolean mandatory;

    private Boolean deprecated;
    private String deprecatedSince;
    private String deprecatedCause;
    private String deprecatedWillBeRemovedOn;

    private List<ConstraintDefinition> constraints;
    private Boolean validationCascaded;

    //    private String notes;
    //    private Boolean required;
    //    private String paramAccess;
    //    private String internalDescription;
    //    private String wrapperName;
    //    private String dataType ;

}
