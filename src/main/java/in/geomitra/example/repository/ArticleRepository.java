package in.geomitra.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import in.geomitra.example.domain.Article;

public interface ArticleRepository extends JpaRepository<Article, Long> {

}
