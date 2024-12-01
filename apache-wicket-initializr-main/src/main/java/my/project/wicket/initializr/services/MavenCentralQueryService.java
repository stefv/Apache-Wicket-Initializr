package my.project.wicket.initializr.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import my.project.wicket.initializr.beans.ArtifactVersionViewBean;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * A service to query Maven Central for the versions of Apache Wicket.
 */
@Service
public class MavenCentralQueryService implements IMavenCentralQueryService {

    /**
     * The Maven Central API URL.
     */
    private static final String MAVEN_CENTRAL_API = "https://search.maven.org/solrsearch/select?q=g:org.apache.wicket+AND+a:wicket-core&core=gav&rows=100&wt=json";

    /**
     * The REST template.
     */
    private final RestTemplate restTemplate;

    /**
     * The object mapper.
     */
    private final ObjectMapper objectMapper;

    /**
     * The version comparator service.
     */
    private final IVersionComparatorService versionComparatorService;

    /**
     * Create a new instance of the MavenCentralQueryService.
     *
     * @param restTemplate The REST template.
     * @param objectMapper The object mapper.
     */
    public MavenCentralQueryService(RestTemplate restTemplate, ObjectMapper objectMapper, IVersionComparatorService versionComparatorService) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.versionComparatorService = versionComparatorService;
    }

    /**
     * {@inheritDoc}
     */
    @Cacheable("wicket-core-versions")
    public List<ArtifactVersionViewBean> fetchWicketCoreVersions() {

        // Make the GET request to Maven Central
        final ResponseEntity<String> response = restTemplate.getForEntity(MAVEN_CENTRAL_API, String.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            // Parse the response JSON and extract versions
            return parseVersions(response.getBody());
        } else {
            throw new RuntimeException("Failed to fetch versions. HTTP status: " + response.getStatusCode());
        }
    }

    /**
     * Parses the JSON response to extract versions from the "docs" node.
     *
     * @param jsonResponse The raw JSON response as a string.
     * @return A list of versions as strings.
     */
    private List<ArtifactVersionViewBean> parseVersions(String jsonResponse) {
        try {
            final JsonNode root = objectMapper.readTree(jsonResponse);
            final JsonNode docs = root.path("response").path("docs");

            final List<ArtifactVersionViewBean> versions = new ArrayList<>();
            for (JsonNode doc : docs) {
                versions.add(new ArtifactVersionViewBean(doc.path("v").asText()));
            }

            versions.sort(versionComparatorService);

            return versions;
        } catch (Exception e) {
            throw new RuntimeException("Error parsing JSON response", e);
        }
    }
}
