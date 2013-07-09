package net.awired.jaxrs.doc.domain.sub;

import lombok.Data;

@Data
public class ErrorResponseDefinition {

    private Integer code = 404;
    private String reason = "No user found in session";

}
