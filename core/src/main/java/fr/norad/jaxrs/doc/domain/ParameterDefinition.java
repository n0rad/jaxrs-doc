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

import javax.xml.bind.annotation.XmlRootElement;
import lombok.Data;

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

    private Boolean deprecated;
    private String deprecatedSince;
    private String deprecatedCause;

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
