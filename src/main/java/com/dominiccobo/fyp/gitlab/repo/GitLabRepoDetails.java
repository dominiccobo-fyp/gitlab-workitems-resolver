package com.dominiccobo.fyp.gitlab.repo;

public class GitLabRepoDetails {
    private final String orgOrUserName;
    private final String projectName;

    // suppress silly jvm warning, this is instantiated via the package-private constructor below
    private GitLabRepoDetails() {
        orgOrUserName = null;
        projectName = null;
    }

    GitLabRepoDetails(String orgOrUserName, String projectName) {
        this.orgOrUserName = orgOrUserName;
        this.projectName = projectName;
    }

    public static GitLabRepoDetails from(String remoteUrl) {

        if(GitLabSSHDetailsExtractor.isGitLabSSHUrl(remoteUrl)) {
            return new GitLabSSHDetailsExtractor(remoteUrl).extract();
        }
        if(GitLabHTTPSDetailsExtractor.isGitLabHTTPSUrl(remoteUrl)) {
            return new GitLabHTTPSDetailsExtractor(remoteUrl).extract();
        }
        return null;
    }

    public String getOrgOrUserName() {
        return this.orgOrUserName;
    }

    public String getProjectName() {
        return this.projectName;
    }
}
