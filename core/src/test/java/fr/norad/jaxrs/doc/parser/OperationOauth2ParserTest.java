package fr.norad.jaxrs.doc.parser;

import static org.fest.assertions.api.Assertions.assertThat;
import org.junit.Test;
import fr.norad.jaxrs.doc.domain.OperationDefinition;
import fr.norad.jaxrs.oauth2.SecuredWithScope;

public class OperationOauth2ParserTest {
    private OperationOauth2Parser operationParser = new OperationOauth2Parser();

    @Test
    public void should_not_fill_secured_for_not_annotated() throws Exception {
        class Test {
            public void getString() {
            }
        }
        OperationDefinition operation = new OperationDefinition();
        operationParser.parse(null, operation, Test.class.getMethod("getString"));

        assertThat(operation.getSecured()).isNull();
    }

    @Test
    public void should_not_fill_secured_for_annotated() throws Exception {
        class Test {
            @SecuredWithScope("genre")
            public void getString() {
            }
        }
        OperationDefinition operation = new OperationDefinition();
        operationParser.parse(null, operation, Test.class.getMethod("getString"));

        assertThat(operation.getSecured()).isNotNull();
        assertThat(operation.getSecured().getScopes()).containsOnly("genre");
    }
}
