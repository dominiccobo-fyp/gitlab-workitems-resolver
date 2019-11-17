package com.dominiccobo.fyp.gitlab;

import com.dominiccobo.fyp.gitlab.repo.GitLabRepoDetails;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class GitLabRepoDetailsTest {

    @Test
    public void testGivenGitLabRemoteURL_whenExtract_returnProjectNameAndOrgName() {
        String upstreamUrl = "git@gitlab.com:dominiccobo-fyp/gitlab-experts-resolver.git";
        String expectedOrg = "dominiccobo-fyp";
        String expectedProject = "gitlab-experts-resolver";
        GitLabRepoDetails details = GitLabRepoDetails.from(upstreamUrl);
        assertThat(details.getOrgOrUserName()).isEqualTo(expectedOrg);
        assertThat(details.getProjectName()).isEqualTo(expectedProject);
    }

    @Test
    public void testGivenNonGitLabRemote_whenExtract_returnNull() {
        String upstreamUrl = "git@github.com:dominiccobo-fyp/gitlab-experts-resolver.git";
        GitLabRepoDetails details = GitLabRepoDetails.from(upstreamUrl);
        assertThat(details).isNull();
    }


}
