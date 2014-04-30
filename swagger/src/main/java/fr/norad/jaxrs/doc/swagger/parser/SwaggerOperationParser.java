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
package fr.norad.jaxrs.doc.swagger.parser;

import static fr.norad.core.lang.StringUtils.notEmpty;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.logging.Logger;
import com.wordnik.swagger.annotations.ApiError;
import com.wordnik.swagger.annotations.ApiErrors;
import com.wordnik.swagger.annotations.ApiOperation;
import fr.norad.core.lang.reflect.AnnotationUtils;
import fr.norad.jaxrs.doc.api.domain.ApiDefinition;
import fr.norad.jaxrs.doc.api.domain.ErrorDefinition;
import fr.norad.jaxrs.doc.api.domain.ErrorOperationDefinition;
import fr.norad.jaxrs.doc.api.domain.OperationDefinition;
import fr.norad.jaxrs.doc.parserapi.OperationParser;

public class SwaggerOperationParser implements OperationParser {

    private Logger log = Logger.getLogger(SwaggerOperationParser.class.getName());

    @Override
    public void parse(ApiDefinition api, OperationDefinition operation, Method method) {
        ApiOperation operationSwagger = AnnotationUtils.findAnnotation(method, ApiOperation.class);
        if (operationSwagger != null) {
            if (notEmpty(operationSwagger.value())) {
                operation.setDescription(operationSwagger.value());
            }
            if (operationSwagger.multiValueResponse()) {
                operation.setResponseAsList(true);
            }
            if (notEmpty(operationSwagger.httpMethod())) {
                operation.setHttpMethod(operationSwagger.httpMethod());
            }
            if (!operationSwagger.responseClass().equals("void")) {
                try {
                    Class<?> responseClass = Class.forName(operationSwagger.responseClass());
                    operation.setResponseClass(responseClass);
                } catch (ClassNotFoundException e) {
                    log.warning("invalid response class from swagger ApiOperation annotation on : " + method);
                }
            }
        }

        ApiErrors errorsSwagger = AnnotationUtils.findAnnotation(method, ApiErrors.class);
        if (errorsSwagger != null) {
            for (ApiError errorSwagger : errorsSwagger.value()) {
                processError(operation, errorSwagger);
            }
        }

        ApiError errorSwagger = AnnotationUtils.findAnnotation(method, ApiError.class);
        if (errorSwagger != null) {
            processError(operation, errorSwagger);
        }
    }

    private void processError(OperationDefinition operation, ApiError errorSwagger) {
        ErrorOperationDefinition error = new ErrorOperationDefinition();//TODO handle already existing error from throws
        error.setReason(errorSwagger.reason());
//        error.setHttpCode(errorSwagger.code());

        if (operation.getErrors() == null) {
            operation.setErrors(new ArrayList<ErrorOperationDefinition>());
        }
        operation.getErrors().add(error);
    }

}
