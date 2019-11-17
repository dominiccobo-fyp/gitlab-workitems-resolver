package com.dominiccobo.fyp.gitlab.repo;

abstract class GitLabDetailsExtractor {
    protected final String remoteUrl;

    GitLabDetailsExtractor(String remoteUrl) {
        this.remoteUrl = remoteUrl;
    }

    abstract GitLabRepoDetails extract();
}
