package com.dominiccobo.fyp.gitlab;

import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Project;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Very basic API integration smoke tests for verifying the functionality we need from the API still exists / works.
 */
public class GitLabApiProviderTest {

    private static final Logger LOG = LoggerFactory.getLogger(GitLabApiProviderTest.class);

    private GitLabApi fixture;

    // TODO; run with SpringRunner class
    // depends on a local instance of gitlab...
    @Before
    public void setUp() throws Exception {
        String gitlabHost = System.getenv("GITLAB_HOST");
        String gitlabToken = System.getenv("GIT_API_KEY");
        fixture = new GitLabApi(GitLabApi.ApiVersion.V4, gitlabHost, gitlabToken);
    }

    @Test
    public void testRetrieveProject() throws GitLabApiException {
        String myNamespace = "gitlab-org";
        String myProject = "gitlab-foss";

        Project project = fixture.getProjectApi().getProject(myNamespace, myProject);
        LOG.info("Retrieved {}/{}: {}", myNamespace, myProject, project.toString());
    }

    @Test
    public void testRetrieveProjectIssues() throws GitLabApiException {
        String myNamespace = "gitlab-org";
        String myProject = "gitlab-foss";

        Project project = fixture.getProjectApi().getProject(myNamespace, myProject);
        Integer projectId = project.getId();
        fixture.getIssuesApi().getIssues(projectId, 20);
        LOG.info("Retrieved issues for {}/{}", myNamespace, myProject);
    }
}
