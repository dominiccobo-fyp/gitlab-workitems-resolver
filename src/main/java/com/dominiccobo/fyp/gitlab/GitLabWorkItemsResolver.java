package com.dominiccobo.fyp.gitlab;

import com.dominiccobo.fyp.context.api.queries.AssociatedWorkItemsQuery;
import com.dominiccobo.fyp.context.listeners.WorkItemQueryListener;
import com.dominiccobo.fyp.context.models.QueryContext;
import com.dominiccobo.fyp.context.models.WorkItem;
import com.dominiccobo.fyp.context.models.git.GitContext;
import com.dominiccobo.fyp.context.models.git.GitRemoteIdentifier;
import com.dominiccobo.fyp.context.models.git.GitRemoteURL;
import com.dominiccobo.fyp.gitlab.provider.GitLabAPI;
import com.dominiccobo.fyp.gitlab.provider.GitLabIssue;
import com.dominiccobo.fyp.gitlab.repo.GitLabRepoDetails;
import org.axonframework.queryhandling.QueryHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class GitLabWorkItemsResolver implements WorkItemQueryListener {
    private static final Logger LOG = LoggerFactory.getLogger(GitLabWorkItemsResolver.class);
    private final GitLabAPI gitLabAPI;

    @Autowired
    public GitLabWorkItemsResolver(GitLabAPI gitLabAPI) {
        this.gitLabAPI = gitLabAPI;
    }

    @QueryHandler
    @Override
    public List<WorkItem> on(AssociatedWorkItemsQuery qry) {
        LOG.info("Received query for associated work items");
        QueryContext queryContext = qry.getContext();
        return fetchWorkItemsFromGitLab(queryContext);
    }

    private List<WorkItem> transformGitLabIssuesToWorkItems(List<GitLabIssue> issues) {
        List<WorkItem> formattedWorkItems = new ArrayList<>();
        for (GitLabIssue issue : issues) {
            formattedWorkItems.add(transformGitLabIssueToWorkItem(issue));
        }
        return formattedWorkItems;
    }

    private WorkItem transformGitLabIssueToWorkItem(GitLabIssue issue) {
        WorkItem workItemToAdd = new WorkItem();
        workItemToAdd.setTitle(issue.getTitle());
        workItemToAdd.setBody(issue.getBody());
        return workItemToAdd;
    }



    private List<WorkItem> fetchWorkItemsFromGitLab(QueryContext qryCtx) {
        if(qryCtx.getGitContext().isPresent()) {
            GitContext gitContext = qryCtx.getGitContext().get();
            List<GitLabIssue> gitLabIssues = fetchWorkItemsForEachRemoteInContext(gitContext);
            return transformGitLabIssuesToWorkItems(gitLabIssues);
        }
        return new ArrayList<>();
    }

    private List<GitLabIssue> fetchWorkItemsForEachRemoteInContext(GitContext gitContext) {
        List<GitLabIssue> retrievedIssues = new ArrayList<>();
        if (gitContext.getRemotes().isPresent()) {
            Map<GitRemoteIdentifier, GitRemoteURL> remotes = gitContext.getRemotes().get();
            for (Map.Entry<GitRemoteIdentifier, GitRemoteURL> remote : remotes.entrySet()) {
                retrievedIssues.addAll(fetchWorkItemsForRemote(remote));
            }
        }
        return retrievedIssues;
    }

    private List<GitLabIssue> fetchWorkItemsForRemote(Map.Entry<GitRemoteIdentifier, GitRemoteURL> remote) {
        String remoteUrl = remote.getValue().getUrl();
        GitLabRepoDetails repoDetails = GitLabRepoDetails.from(remoteUrl);

        if(repoDetails == null) {
            return new ArrayList<>();
        }
        LOG.info("Fetching any issues associated with {} ({}:{})", remoteUrl, repoDetails.getOrgOrUserName(), repoDetails.getProjectName());
        return new ArrayList<>(gitLabAPI.getIssuesForRepository(repoDetails));
    }
}
