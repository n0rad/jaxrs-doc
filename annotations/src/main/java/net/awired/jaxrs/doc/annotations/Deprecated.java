package net.awired.jaxrs.doc.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ java.lang.annotation.ElementType.CONSTRUCTOR, java.lang.annotation.ElementType.FIELD,
        java.lang.annotation.ElementType.LOCAL_VARIABLE, java.lang.annotation.ElementType.METHOD,
        java.lang.annotation.ElementType.PACKAGE, java.lang.annotation.ElementType.PARAMETER,
        java.lang.annotation.ElementType.TYPE })
public @interface Deprecated {

    //TODO message
    // TODO version deprected
    //TODO  replaced by

}
