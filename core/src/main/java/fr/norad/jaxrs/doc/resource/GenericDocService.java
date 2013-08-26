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
package fr.norad.jaxrs.doc.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import fr.norad.jaxrs.doc.DocConfig;
import fr.norad.jaxrs.doc.domain.ProjectDefinition;

@Path("/doc")
public abstract class GenericDocService {

    private ProjectDefinition projectDefinition;

    protected abstract DocConfig getDocConfig();

    @GET
    @Path("/definition")
    public ProjectDefinition getProjectDefinition() {
        if (projectDefinition == null) {
            projectDefinition = getDocConfig().getProjectParser().parse();
        }
        return projectDefinition;
    }

}
