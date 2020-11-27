package in.geomitra.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.geomitra.example.domain.Article;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

}
