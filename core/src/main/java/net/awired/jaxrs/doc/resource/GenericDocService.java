/**
 *
 *     Copyright (C) Awired.net
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
package net.awired.jaxrs.doc.resource;

import java.io.InputStream;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import net.awired.jaxrs.doc.DocConfig;
import net.awired.jaxrs.doc.DocTemplate;
import net.awired.jaxrs.doc.domain.ProjectDefinition;

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

    @GET
    public InputStream getTemplate() {
        return getFile(getDocConfig().getTemplate().getHomePageName());
    }

    @GET
    @Path("/{file}")
    public InputStream getFile(@PathParam("file") String file) {
        DocTemplate template = getDocConfig().getTemplate();
        if (!template.getFiles().contains(file)) {
            throw new NotFoundException();
        }
        return getClass().getResourceAsStream(template.getFileResourcePath() + '/' + file);
    }

}
