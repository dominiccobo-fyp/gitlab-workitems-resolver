package com.dominiccobo.fyp.gitlab.repo;

class GitLabSSHDetailsExtractor extends GitLabDetailsExtractor {
    // FIXME: this is a BIGGGGG lie, what about if they're using a hosted solution ;)
    private static final String GITLAB_SSH_PREFIX = "git@gitlab.com:";
    private static final String SSH_SUFFIX = ".git";

    GitLabSSHDetailsExtractor(String remoteUrl) {
        super(remoteUrl);
    }

    GitLabRepoDetails extract() {
        String orgOrUser = extractOrgOrUserFromSSHRemoteUrl(this.remoteUrl);
        String projectName = extractProjectFromSSHRemoteURL(remoteUrl);
        return new GitLabRepoDetails(orgOrUser, projectName);
    }

    private static String extractOrgOrUserFromSSHRemoteUrl(String remoteUrl) {
        String urlWithPrefixRemoved = removeSuffixAndPrefix(remoteUrl);
        int indexWithOrgOrUser = 0;
        String[] orgOrUserAndProjectSplit = sshPathWithOrgProjectSplit(urlWithPrefixRemoved);
        return orgOrUserAndProjectSplit[indexWithOrgOrUser];
    }

    private static String extractProjectFromSSHRemoteURL(String remoteUrl) {
        String urlWithPrefixRemoved = removeSuffixAndPrefix(remoteUrl);
        int indexWithProject = 1;
        String[] orgOrUserAndProjectSplit = sshPathWithOrgProjectSplit(urlWithPrefixRemoved);
        return orgOrUserAndProjectSplit[indexWithProject];
    }

    private static String[] sshPathWithOrgProjectSplit(String urlWithPrefixRemoved) {
        return urlWithPrefixRemoved.split("/");
    }

    private static String removePrefix(String remoteUrl) {
        return remoteUrl.replace(GITLAB_SSH_PREFIX, "");
    }

    private static String removeSuffix(String remoteUrl) {
        return remoteUrl.replace(SSH_SUFFIX, "");
    }

    private static String removeSuffixAndPrefix(String remoteUrl) {
        String prefixRemoved = removePrefix(remoteUrl);
        String prefixAndSuffixRemoved = removeSuffix(prefixRemoved);
        return prefixAndSuffixRemoved;
    }

    static boolean isGitLabSSHUrl(String remoteUrl) {
        return remoteUrl.contains(GITLAB_SSH_PREFIX);
    }
}
