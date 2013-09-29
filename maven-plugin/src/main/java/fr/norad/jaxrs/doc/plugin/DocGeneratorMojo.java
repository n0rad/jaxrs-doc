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
package fr.norad.jaxrs.doc.plugin;

import java.io.File;
import java.util.Arrays;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospectorPair;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;
import fr.norad.jaxrs.doc.JaxRsDocProcessorFactory;
import fr.norad.jaxrs.doc.domain.ProjectDefinition;
import fr.norad.jaxrs.doc.parser.ModelJacksonParser;

@Mojo(name = "generate-doc", defaultPhase = LifecyclePhase.PROCESS_CLASSES, configurator = "include-project-dependencies", requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME)
public class DocGeneratorMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project.build.directory}/jaxrs-doc-${version}.json", property = "outputFile", required = false)
    private File outputFile;

    @Parameter(defaultValue = "${project.version}", property = "version", required = false)
    private String version;

    @Parameter(defaultValue = "${packageIncludes}", property = "packageIncludes", required = true)
    private String[] packageIncludes;

    private ObjectMapper objectMapper;

    public DocGeneratorMojo() {
        objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(Include.NON_NULL);
        objectMapper.setAnnotationIntrospector(new AnnotationIntrospectorPair(new JacksonAnnotationIntrospector(),
                new JaxbAnnotationIntrospector(TypeFactory.defaultInstance())));
    }

    @Override
    public void execute() throws MojoExecutionException {
        outputFile = new File(outputFile.getAbsolutePath().replace("${version}", version));
        try {
            JaxRsDocProcessorFactory docConfig = new JaxRsDocProcessorFactory(Arrays.asList(packageIncludes));
            docConfig.setModelParser(new ModelJacksonParser(docConfig));
            writeDefinitionToFile(docConfig.getProjectParser().parse());
        } catch (Exception e) {
            getLog().error(e);
            throw new MojoExecutionException("Jaxrs doc generation fail", e);
        }
    }

    private void writeDefinitionToFile(ProjectDefinition definition) {
        if (!outputFile.getParentFile().exists()) {
            outputFile.getParentFile().mkdirs();
        }

        try {
            getLog().info("Writing project definition to file : " + outputFile);
            objectMapper.writer().writeValue(outputFile, definition);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot write project definition to file : " + outputFile, e);
        }
    }

}
