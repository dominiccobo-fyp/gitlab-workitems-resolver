package com.dominiccobo.fyp.gitlab.provider;

import com.dominiccobo.fyp.gitlab.repo.GitLabRepoDetails;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.Pager;
import org.gitlab4j.api.models.Issue;
import org.gitlab4j.api.models.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class GitLabAPIProvider implements GitLabAPI {

    private static final Logger LOG = LoggerFactory.getLogger(GitLabAPIProvider.class);
    private final GitLabApi apiProvider;

    @Autowired
    public GitLabAPIProvider(GitLabApi apiProvider) {
        this.apiProvider = apiProvider;
    }

    @Override
    public List<GitLabIssue> getIssuesForRepository(GitLabRepoDetails repoDetails) {
        return getProjectIssues(repoDetails).stream().map(ProviderGitLabIssue::new).collect(Collectors.toList());
    }

    private List<Issue> getProjectIssues(GitLabRepoDetails repoDetails) {

        LinkedList<Issue> allIssues = new LinkedList<>();
        try {
            Project project = apiProvider
                    .getProjectApi()
                    .getProject(repoDetails.getOrgOrUserName(), repoDetails.getOrgOrUserName());

            if (project.getIssuesEnabled()) {
                Integer projectId = project.getId();
                Pager<Issue> issues = apiProvider.getIssuesApi().getIssues(projectId, 100);

                while (issues.hasNext()) {
                    allIssues.addAll(issues.next());
                }
            }
        }
        catch (GitLabApiException e) {
            LOG.warn("Could not retrieve issues from GitLab API for {}:{}", repoDetails.getOrgOrUserName(), repoDetails.getProjectName(), e);
            return new ArrayList<>();
        }
        return allIssues;
    }

    private static class ProviderGitLabIssue implements GitLabIssue {

        private final Issue issue;

        private ProviderGitLabIssue(Issue issue) {
            this.issue = issue;
        }

        @Override
        public String getTitle() {
            return issue.getTitle();
        }

        @Override
        public String getBody() {
            return issue.getDescription();
        }
    }
}
