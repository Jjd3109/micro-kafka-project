package micro.kafka.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import micro.kafka.common.outboxmessagerelay.OutboxEvent;
import micro.kafka.common.outboxmessagerelay.OutboxEventRepository;
import micro.kafka.dto.request.ArticleCreateRequest;
import micro.kafka.dto.request.ArticleUpdateRequest;
import micro.kafka.dto.response.ArticleResponse;
import micro.kafka.entity.Article;
import micro.kafka.repository.ArticleRepository;

@Service
@RequiredArgsConstructor
@Log4j2
public class ArticleService {

	private final ArticleRepository articleRepository;
	private final OutboxEventRepository outboxEventRepository;

	@Transactional
	public ArticleResponse create(ArticleCreateRequest request) {
		Article article = articleRepository.save(
			Article.create(
				request.getTitle(),
				request.getContent(),
				request.getBoardId(),
				request.getWriterId()
			)
		);

		outboxEventRepository.save(
			OutboxEvent.create("ARTICLE_CREATED", article, article.getArticleId())
		);



		return ArticleResponse.from(article);
	}

	@Transactional
	public ArticleResponse update(Long articleId, ArticleUpdateRequest request){
		Article article = articleRepository.findById(articleId).orElseThrow();
		article.update(request.getTitle(), request.getContent());

		return  ArticleResponse.from(article);

	}

	public ArticleResponse read(Long articleId){
		return ArticleResponse.from(articleRepository.findById(articleId).orElseThrow());
	}
}
