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
import fr.norad.jaxrs.oauth2.SecuredInfo;
import lombok.Data;

@Data
@XmlRootElement
public class OperationDefinition {

    private Class<?> operationClass;
    private String path;

    private String httpMethod;
    private String description;
    private String methodName;
    private Map<String, Object> extras;

    private Boolean deprecated;
    private String deprecatedSince;
    private String deprecatedCause;
    private String deprecatedWillBeRemovedOn;

    private Class<?> responseClass;
    private Class<?> responseMapKeyClass;
    private Boolean responseAsList;
    private String summary;
    private SecuredInfo securedInfo;

    private List<ParameterDefinition> parameters;
    private List<ErrorOperationDefinition> errors;

    private Integer businessCode;
    private String reason;


    private List<String> consumes;
    private List<String> produces;


    public OperationDefinition() {
    }

    public OperationDefinition(String path) {
        this.path = path;
    }

    public ErrorOperationDefinition findErrorDefinition(Class<?> errorClass) {
        if (errors == null) {
            return null;
        }
        for (ErrorOperationDefinition definition : errors) {
            if (errorClass.equals(definition.getErrorClass())) {
                return definition;
            }
        }
        return null;
    }

}
