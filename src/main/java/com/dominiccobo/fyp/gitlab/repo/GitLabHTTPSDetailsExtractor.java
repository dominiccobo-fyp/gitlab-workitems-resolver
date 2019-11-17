package com.dominiccobo.fyp.gitlab.repo;

class GitLabHTTPSDetailsExtractor extends GitLabDetailsExtractor {

    // FIXME: this is a BIGGGGG lie, what about if they're using a hosted solution ;)
    private static final String GITLAB_HTTPS_PREFIX = "https://gitlab.com/";

    GitLabHTTPSDetailsExtractor(String remoteUrl) {
        super(remoteUrl);
    }

    GitLabRepoDetails extract() {
        String orgOrUser = parseOrgOrUserFromURL(this.remoteUrl);
        String projectName = parseProjectFromURL(this.remoteUrl);
        return new GitLabRepoDetails(orgOrUser, projectName);
    }

    private static String parseProjectFromURL(String remoteUrl) {
        int projectIndex = 1;
        return removePrefix(remoteUrl).split("/")[projectIndex];
    }

    private static String parseOrgOrUserFromURL(String remoteUrl) {
        int orgOrUserIndex = 0;
        return removePrefix(remoteUrl).split("/")[orgOrUserIndex];
    }

    private static String removePrefix(String remoteUrl) {
        return remoteUrl.replaceAll(GITLAB_HTTPS_PREFIX, "");
    }

    static boolean isGitLabHTTPSUrl(String remoteUrl) {
        return remoteUrl.contains(GITLAB_HTTPS_PREFIX);
    }
}
