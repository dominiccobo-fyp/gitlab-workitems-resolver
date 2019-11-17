package com.dominiccobo.fyp.gitlab;

import com.dominiccobo.fyp.context.api.queries.AssociatedWorkItemsQuery;
import com.dominiccobo.fyp.context.models.QueryContext;
import com.dominiccobo.fyp.context.models.WorkItem;
import com.dominiccobo.fyp.context.models.git.GitContext;
import com.dominiccobo.fyp.context.models.git.GitRemoteIdentifier;
import com.dominiccobo.fyp.context.models.git.GitRemoteURL;
import com.dominiccobo.fyp.gitlab.provider.GitLabAPI;
import com.dominiccobo.fyp.gitlab.provider.GitLabIssue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Matchers.any;

@RunWith(MockitoJUnitRunner.class)
public class GitLabWorkItemsResolverTest {

    private GitLabWorkItemsResolver fixture;
    @Mock
    private GitLabAPI gitLabAPI;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        fixture = new GitLabWorkItemsResolver(gitLabAPI);
    }

    @Test
    public void givenNonGitlabUrl_whenRetrieveResults_expectAnEmptyList() {
        AssociatedWorkItemsQuery qry = queryItemForSingleUpstream("git@github.com:dominiccobo/cs3004-assignment.git");
        List<WorkItem> result = fixture.on(qry);
        assertThat(result).isEmpty();
    }

    @Test
    public void givenUserRepoExists_whenQueryMadeForRemoteWorkItems_singleWorkItemRetrievedFromUserRepo() {
        final String WORK_ITEM_TITLE = "MyWorkItem";
        final String WORK_ITEM_BODY = "My work item content";

        Mockito.when(gitLabAPI.getIssuesForRepository(any())).thenReturn(Collections.singletonList(new MockableIssue(WORK_ITEM_TITLE, WORK_ITEM_BODY)));
        AssociatedWorkItemsQuery qry = queryItemForSingleUpstream("git@gitlab.com:dominiccobo/cs3004-assignment.git");

        WorkItem expectedWorkItem = new WorkItem().setBody(WORK_ITEM_BODY).setTitle(WORK_ITEM_TITLE);

        List<WorkItem> result = fixture.on(qry);
        assertThat(result).hasSize(1);
        assertThat(result).contains(expectedWorkItem);
    }

    public AssociatedWorkItemsQuery queryItemForSingleUpstream(String upstreamUrl) {
        Map<GitRemoteIdentifier, GitRemoteURL> remotes = new HashMap<>();
        remotes.put(new GitRemoteIdentifier("upstream"), new GitRemoteURL(upstreamUrl));
        GitContext gitContext = new GitContext(remotes, null);
        QueryContext queryContext = new QueryContext(gitContext, null);
        return new AssociatedWorkItemsQuery(queryContext);
    }

    static class MockableIssue implements GitLabIssue {

        private final String title;
        private final String body;

        MockableIssue(String title, String body) {
            this.title = title;
            this.body = body;
        }

        @Override
        public String getTitle() {
            return title;
        }

        @Override
        public String getBody() {
            return body;
        }
    }
}
