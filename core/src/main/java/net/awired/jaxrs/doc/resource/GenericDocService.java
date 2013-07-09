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
