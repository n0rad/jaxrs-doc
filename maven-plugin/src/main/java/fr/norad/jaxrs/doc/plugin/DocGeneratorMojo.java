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

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.Arrays;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import fr.norad.jaxrs.doc.DocConfig;
import fr.norad.jaxrs.doc.domain.ProjectDefinition;
import fr.norad.jaxrs.doc.parser.ModelJacksonParser;

@Mojo(name = "generate-doc", defaultPhase = LifecyclePhase.PROCESS_CLASSES, configurator = "include-project-dependencies", requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME)
public class DocGeneratorMojo extends AbstractMojo {

    @Parameter(defaultValue = "${packageIncludes}", property = "packageIncludes", required = true)
    private String[] packageIncludes;

    @Override
    public void execute() throws MojoExecutionException {
        try {
            DocConfig docConfig = new DocConfig(Arrays.asList(packageIncludes));
            docConfig.setModelParser(new ModelJacksonParser(docConfig));
            ProjectDefinition projectDefinition = docConfig.getProjectParser().parse();

            System.out.println(projectDefinition);

            //            readErrors(reflections);
            //
            //            Map<String, Documentation> endPoints = readEndPoints(reflections);
            //
            //            Documentation docIndex = buildIndex(endPoints);
            //
            //            writeFiles(docIndex, endPoints, outputDirectory);
        } catch (Exception e) {
            getLog().error(e);
            throw new MojoExecutionException("Jaxrs doc generation fail", e);
        }
    }

}
