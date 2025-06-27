package nova.hackathon.util.ElasticSearch.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "nova.hackathon.util.ElasticSearch.Repository")
public class ElasticsearchConfig {}
