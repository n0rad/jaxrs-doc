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

import static fr.norad.jaxrs.doc.parser.OperationOauth2ParserTest.SecuringSomething.TheScopes.SCOPE_A;
import static fr.norad.jaxrs.oauth2.ScopeStrategy.ALL;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.fest.assertions.api.Assertions.assertThat;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import org.junit.Test;
import fr.norad.jaxrs.doc.domain.OperationDefinition;
import fr.norad.jaxrs.oauth2.Scope;
import fr.norad.jaxrs.oauth2.ScopeStrategy;
import fr.norad.jaxrs.oauth2.Secured;

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

        assertThat(operation.getSecuredInfo()).isNull();
    }

    @Test
    public void should_fill_secured_for_annotated() throws Exception {
        class Test {
            @SecuringSomething(SCOPE_A)
            public void getString() {
            }
        }
        OperationDefinition operation = new OperationDefinition();
        operationParser.parse(null, operation, Test.class.getMethod("getString"));

        assertThat(operation.getSecuredInfo()).isNotNull();
        assertThat(operation.getSecuredInfo().getScopes()).containsOnly(SCOPE_A);
    }

    @Secured
    @Target({ METHOD, TYPE })
    @Retention(RUNTIME)
    public @interface SecuringSomething {

        TheScopes[] value();

        ScopeStrategy strategy() default ALL;

        public enum TheScopes implements Scope {
            SCOPE_A,
            SCOPE_B,
            SCOPE_C;

            @Override
            public String scopeIdentifier() {
                return this.name();
            }
        }
    }

}
