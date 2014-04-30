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
package fr.norad.jaxrs.doc.parser;

import static fr.norad.core.lang.StringUtils.notEmpty;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.Map;
import javax.ws.rs.BeanParam;
import javax.ws.rs.CookieParam;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.Encoded;
import javax.ws.rs.FormParam;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import fr.norad.core.lang.reflect.AnnotationUtils;
import fr.norad.jaxrs.doc.api.domain.LocalizationDefinition;
import fr.norad.jaxrs.doc.api.domain.ParameterDefinition;
import fr.norad.jaxrs.doc.api.domain.ParameterType;
import fr.norad.jaxrs.doc.parserapi.ParameterParser;

public class ParameterJaxrsParser implements ParameterParser {

    @Override
    public void parse(Map<Locale, LocalizationDefinition> localeDefinitions, ParameterDefinition parameter,
                      Method method, int position) {
        fillTypeAndValue(parameter, method, position);

        Encoded encoded = AnnotationUtils.findParameterAnnotation(method, position, Encoded.class);
        if (encoded != null) {
            parameter.setEncoded(true);
        }

        DefaultValue defValue = AnnotationUtils.findParameterAnnotation(method, position, DefaultValue.class);
        if (defValue != null && notEmpty(defValue.value())) {
            parameter.setDefaultValue(defValue.value());
        }
    }

    private void fillTypeAndValue(ParameterDefinition param, Method method, int position) {
        Context context = AnnotationUtils.findParameterAnnotation(method, position, Context.class);
        if (context != null) {
            param.setType(ParameterType.CONTEXT); //TODO SPECIAL PARSE
            return;
        }

        BeanParam bean = AnnotationUtils.findParameterAnnotation(method, position, BeanParam.class);
        if (bean != null) {
            param.setType(ParameterType.BEAN); //TODO SPECIAL PARSE
            return;
        }

        PathParam path = AnnotationUtils.findParameterAnnotation(method, position, PathParam.class);
        if (path != null) {
            param.setType(ParameterType.PATH);
            param.setName(path.value());
            return;
        }

        QueryParam query = AnnotationUtils.findParameterAnnotation(method, position, QueryParam.class);
        if (query != null) {
            param.setType(ParameterType.QUERY);
            param.setName(query.value());
            return;
        }

        MatrixParam matrix = AnnotationUtils.findParameterAnnotation(method, position, MatrixParam.class);
        if (matrix != null) {
            param.setType(ParameterType.MATRIX);
            param.setName(matrix.value());
            return;
        }

        FormParam form = AnnotationUtils.findParameterAnnotation(method, position, FormParam.class);
        if (form != null) {
            param.setType(ParameterType.FORM);
            param.setName(form.value());
            return;
        }

        CookieParam cookie = AnnotationUtils.findParameterAnnotation(method, position, CookieParam.class);
        if (cookie != null) {
            param.setType(ParameterType.COOKIE);
            param.setName(cookie.value());
            return;
        }

        HeaderParam header = AnnotationUtils.findParameterAnnotation(method, position, HeaderParam.class);
        if (header != null) {
            param.setType(ParameterType.HEADER);
            param.setName(header.value());
            return;
        }

        param.setType(ParameterType.REQUEST_BODY);
    }
}
