package net.awired.jaxrs.doc;

import static net.awired.jaxrs.doc.DocTemplate.GITHUB;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import lombok.Data;
import net.awired.jaxrs.doc.parser.ApiParser;
import net.awired.jaxrs.doc.parser.ModelJacksonParser;
import net.awired.jaxrs.doc.parser.ModelParser;
import net.awired.jaxrs.doc.parser.OperationParser;
import net.awired.jaxrs.doc.parser.ParameterParser;
import net.awired.jaxrs.doc.parser.ProjectParser;
import net.awired.jaxrs.doc.parser.PropertyParser;

@Data
public class DocConfig {

    private final List<Pattern> ignoreModelPatterns = new ArrayList<>();

    // parsers
    private ProjectParser projectParser = new ProjectParser(this);
    private ApiParser apiParser = new ApiParser(this);
    private OperationParser operationParser = new OperationParser(this);
    private ParameterParser parameterParser = new ParameterParser(this);
    private ModelParser modelParser = new ModelJacksonParser(this);
    private PropertyParser propertyParser = new PropertyParser(this);
    // template
    private DocTemplate template = GITHUB;
    // project
    private List<String> projectPackagesToScan;

    //project.type   scan class or interface or both ?? 

    public DocConfig(List<String> packageToScan) {
        projectPackagesToScan = packageToScan;
        ignoreModelPatterns.add(Pattern.compile("java\\.[lang|util|io]+\\.[\\w\\._-]+"));
        ignoreModelPatterns.add(Pattern.compile("void|int|long|byte|char|short|boolean|float|double"));
        ignoreModelPatterns.add(Pattern.compile("javax\\.ws\\.rs\\.core\\.Response"));
    }

    public void addIgnoreModelPattern(Pattern pattern) {
        ignoreModelPatterns.add(pattern);
    }

}
