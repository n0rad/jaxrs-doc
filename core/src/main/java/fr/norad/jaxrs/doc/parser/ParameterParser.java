/**
 *
 *     Copyright (C) Awired.net
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

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import javax.ws.rs.BeanParam;
import javax.ws.rs.CookieParam;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.Encoded;
import javax.ws.rs.FormParam;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import fr.norad.jaxrs.doc.DocConfig;
import fr.norad.jaxrs.doc.annotations.Description;
import fr.norad.jaxrs.doc.domain.ParameterDefinition;
import fr.norad.jaxrs.doc.domain.ProjectDefinition;
import fr.norad.jaxrs.doc.domain.sub.ParameterType;
import fr.norad.jaxrs.doc.utils.AnnotationUtil;
import fr.norad.jaxrs.doc.utils.ReflectionUtil;

public class ParameterParser {

    private final DocConfig docConfig;

    public ParameterParser(DocConfig docConfig) {
        this.docConfig = docConfig;
    }

    public ParameterDefinition parse(ProjectDefinition project, Method method, int position) {
        ParameterDefinition param = new ParameterDefinition();

        fillTypeAndValue(param, method, position);

        Encoded encoded = AnnotationUtil.findParameterAnnotation(method, position, Encoded.class);
        param.setEncoded(encoded == null ? null : true);

        Description desc = AnnotationUtil.findParameterAnnotation(method, position, Description.class);
        param.setDescription(desc == null ? null : desc.value());

        DefaultValue defValue = AnnotationUtil.findParameterAnnotation(method, position, DefaultValue.class);
        param.setDefaultValue(defValue == null ? null : defValue.value());

        fillParamClassPart(param, method, position);
        docConfig.getModelParser().parse(project, param.getParamClass());

        return param;
    }

    private void fillParamClassPart(ParameterDefinition param, Method method, int position) {
        if (Map.class.isAssignableFrom(method.getReturnType())) {
            param.setParamMapKeyClass(ReflectionUtil.getGenericParamTypeForPosition(method, position, 0));
            param.setParamClass(ReflectionUtil.getGenericParamTypeForPosition(method, position, 1));
            param.setParamAsList(true);
            return;
        }
        if (Collection.class.isAssignableFrom(method.getParameterTypes()[position])) {
            param.setParamClass(ReflectionUtil.getSingleGenericParamType(method, position));
            param.setParamAsList(true);
            return;
        }
        if (method.getParameterTypes()[position].isArray()) {
            param.setParamClass(method.getParameterTypes()[position].getComponentType());
            param.setParamAsList(true);
            return;
        }

        param.setParamClass(method.getParameterTypes()[position]);
    }

    private void fillTypeAndValue(ParameterDefinition param, Method method, int position) {
        Context context = AnnotationUtil.findParameterAnnotation(method, position, Context.class);
        if (context != null) {
            param.setParamType(ParameterType.CONTEXT); //TODO SPECIAL PARSE
            return;
        }

        BeanParam bean = AnnotationUtil.findParameterAnnotation(method, position, BeanParam.class);
        if (bean != null) {
            param.setParamType(ParameterType.BEAN); //TODO SPECIAL PARSE
            return;
        }

        PathParam path = AnnotationUtil.findParameterAnnotation(method, position, PathParam.class);
        if (path != null) {
            param.setParamType(ParameterType.PATH);
            param.setParamName(path.value());
            return;
        }

        QueryParam query = AnnotationUtil.findParameterAnnotation(method, position, QueryParam.class);
        if (query != null) {
            param.setParamType(ParameterType.QUERY);
            param.setParamName(query.value());
            return;
        }

        MatrixParam matrix = AnnotationUtil.findParameterAnnotation(method, position, MatrixParam.class);
        if (matrix != null) {
            param.setParamType(ParameterType.MATRIX);
            param.setParamName(matrix.value());
            return;
        }

        FormParam form = AnnotationUtil.findParameterAnnotation(method, position, FormParam.class);
        if (form != null) {
            param.setParamType(ParameterType.FORM);
            param.setParamName(form.value());
            return;
        }

        CookieParam cookie = AnnotationUtil.findParameterAnnotation(method, position, CookieParam.class);
        if (cookie != null) {
            param.setParamType(ParameterType.COOKIE);
            param.setParamName(cookie.value());
            return;
        }

        param.setParamType(ParameterType.REQUEST_BODY);
    }

}
