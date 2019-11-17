package com.dominiccobo.fyp.gitlab.provider;

import com.dominiccobo.fyp.gitlab.repo.GitLabRepoDetails;

import java.util.List;

public interface GitLabAPI {

    List<GitLabIssue> getIssuesForRepository(GitLabRepoDetails repoDetails);
}
