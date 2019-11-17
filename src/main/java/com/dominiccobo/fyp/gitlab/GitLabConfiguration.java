package com.dominiccobo.fyp.gitlab;

import org.gitlab4j.api.GitLabApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GitLabConfiguration {

    @Bean
    public GitLabApi gitlabApiProvider(@Value("${gitlab.hostUrl}") String hostUrl,
                                       @Value("${gitlab.apiToken}") String apiAccessToken) {
        return new GitLabApi(GitLabApi.ApiVersion.V4, hostUrl, apiAccessToken);
    }
}
