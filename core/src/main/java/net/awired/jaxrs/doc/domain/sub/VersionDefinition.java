package net.awired.jaxrs.doc.domain.sub;

import lombok.Data;

@Data
public class VersionDefinition {

    private String since = "2.17.0";
    private String deprecatedSince = null;
    private String replacedBy = null;

}
