package fr.norad.jaxrs.doc.domain;


import fr.norad.jaxrs.doc.annotations.OperationError;
import lombok.Data;

@Data
public class ErrorOperationDefinition {
    private Class<? extends Exception> errorClass;
    private String reason;

    public ErrorOperationDefinition() {
    }

    public ErrorOperationDefinition(Class<? extends Exception> e) {
        errorClass = e;
    }

    public ErrorOperationDefinition(OperationError operationError) {
        errorClass = operationError.errorClass();
        reason = operationError.reason();
    }
}
