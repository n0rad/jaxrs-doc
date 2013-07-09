package net.awired.jaxrs.doc.domain.sub;

public enum ParameterType {
    //    path, body, query, matrix, header, form, cookie, ;

    PATH, //
    QUERY, //
    MATRIX, //
    HEADER, //
    COOKIE, //
    FORM, //
    BEAN, // TODO: process bean and remove it from here

    REQUEST_BODY, //
    CONTEXT, //
    UNKNOWN //

    ;
}
