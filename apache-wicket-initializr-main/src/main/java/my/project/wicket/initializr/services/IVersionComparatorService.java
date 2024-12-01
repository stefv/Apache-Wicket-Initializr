package my.project.wicket.initializr.services;

import my.project.wicket.initializr.beans.ArtifactVersionViewBean;

import java.util.Comparator;

/**
 * A service to compare versions of Apache Wicket.
 */
public interface IVersionComparatorService extends Comparator<ArtifactVersionViewBean> {
}
