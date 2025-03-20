package micro.kafka.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import micro.kafka.entity.Article;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
