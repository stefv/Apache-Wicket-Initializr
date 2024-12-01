package my.project.wicket.initializr.services;

import my.project.wicket.initializr.beans.ArtifactVersionViewBean;

import java.util.List;

/**
 * The Maven Central query service.
 */
public interface IMavenCentralQueryService {

    /**
     * Fetches all available versions of 'apache.wicket:wicket-core' from Maven Central.
     *
     * @return A list of versions.
     */
    List<ArtifactVersionViewBean> fetchWicketCoreVersions();
}
