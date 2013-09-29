package fr.norad.jaxrs.doc.parser;

import java.lang.reflect.Method;
import javax.ws.rs.BeanParam;
import javax.ws.rs.CookieParam;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.Encoded;
import javax.ws.rs.FormParam;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import fr.norad.jaxrs.doc.domain.ParameterDefinition;
import fr.norad.jaxrs.doc.domain.ParameterType;
import fr.norad.jaxrs.doc.parserapi.ParameterParser;
import fr.norad.jaxrs.doc.utils.AnnotationUtil;

public class ParameterJaxrsParser implements ParameterParser {

    @Override
    public void parse(ParameterDefinition parameter, Method method, int position) {
        fillTypeAndValue(parameter, method, position);

        Encoded encoded = AnnotationUtil.findParameterAnnotation(method, position, Encoded.class);
        parameter.setEncoded(encoded == null ? null : true);

        DefaultValue defValue = AnnotationUtil.findParameterAnnotation(method, position, DefaultValue.class);
        parameter.setDefaultValue(defValue == null ? null : defValue.value());
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
