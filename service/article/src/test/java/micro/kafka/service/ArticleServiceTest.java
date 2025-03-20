package micro.kafka.service;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;
import micro.kafka.dto.request.ArticleCreateRequest;
import micro.kafka.dto.request.ArticleUpdateRequest;
import micro.kafka.dto.response.ArticleResponse;
import micro.kafka.entity.Article;
import micro.kafka.repository.ArticleRepository;

@SpringBootTest
@Slf4j
class ArticleServiceTest {

	@Autowired
	ArticleService articleService;

	@Autowired
	ArticleRepository articleRepository;

	@AfterEach
	void DB_내용_삭제(){
		articleRepository.deleteAll();
	}

	@Test
	void 게시글_생성() throws InterruptedException {
		//given
		int threadCount = 100; // 스레드 카운트 100
		ExecutorService executorService = Executors.newFixedThreadPool(32);
		CountDownLatch countDownLatch = new CountDownLatch(threadCount);

		//when
		for(int i = 0; i < threadCount; i++){

			ArticleCreateRequest articleCreateRequest = new ArticleCreateRequest(
				"title" + i,
				"content" + i,
				1L,
				1L
			);

			executorService.submit(() -> {
				try{
					articleService.create(articleCreateRequest);
				}finally {
					countDownLatch.countDown();
				}

			});
		}
		countDownLatch.await();

		//then
		assertThat(articleRepository.count()).isEqualTo(100);

	}

	@Test
	void 게시글_수정() throws InterruptedException {
		// given
		int threadCount = 100;
		ExecutorService executorService = Executors.newFixedThreadPool(32);
		CountDownLatch createLatch = new CountDownLatch(threadCount);
		CountDownLatch updateLatch = new CountDownLatch(threadCount);

		// 게시글 ID 저장용 리스트
		List<Long> articleIds = Collections.synchronizedList(new ArrayList<>());

		// when

		// 게시글 생성
		for (int i = 0; i < threadCount; i++) {
			int index = i;

			executorService.submit(() -> {
				try {
					ArticleCreateRequest request = new ArticleCreateRequest(
						"title" + index,
						"content" + index,
						1L,
						1L
					);

					ArticleResponse response = articleService.create(request);
					articleIds.add(response.getArticleId()); // 생성된 ID 저장
				} finally {
					createLatch.countDown();
				}
			});
		}
		createLatch.await(); // 모든 쓰레드가 끝날 때까지 대기

		// 게시글 수정
		for (int i = 0; i < threadCount; i++) {
			int index = i;
			executorService.submit(() -> {
				try {
					if (index < articleIds.size()) {
						Long articleId = articleIds.get(index);

						ArticleUpdateRequest updateRequest = new ArticleUpdateRequest(
							"title update " + index,
							"content update " + index
						);

						articleService.update(articleId, updateRequest);
					}
				} finally {
					updateLatch.countDown();
				}
			});
		}
		updateLatch.await(); // 모든 업데이트 작업 완료 대기

		// then: 데이터 검증
		for (Long articleId : articleIds) {
			Article updatedArticle = articleRepository.findById(articleId).orElseThrow();
			assertThat(updatedArticle.getTitle()).startsWith("title update");
			assertThat(updatedArticle.getContent()).startsWith("content update");
		}
	}


}