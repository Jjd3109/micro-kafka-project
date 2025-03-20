package micro.kafka.service;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import lombok.extern.slf4j.Slf4j;
import micro.kafka.common.outboxmessagerelay.OutboxEvent;
import micro.kafka.common.outboxmessagerelay.OutboxEventRepository;
import micro.kafka.dto.request.ArticleCreateRequest;

@Slf4j
@SpringBootTest
public class ArticleKafkaServiceTest {

	@Autowired
	private ArticleService articleService;

	@Autowired
	private OutboxEventRepository outboxEventRepository;

	@Test
	void Outbox_이벤트_저장_후_Kafka_발행_테스트() throws InterruptedException {
		// given
		ArticleCreateRequest request = new ArticleCreateRequest("Test Title", "Test Content", 1L, 1L);
		articleService.create(request);

		// when
		Thread.sleep(5000); // Kafka가 메시지를 처리할 시간을 줌

		// then
		List<OutboxEvent> events = outboxEventRepository.findAll();


		for (OutboxEvent event : events) {
			log.info("event값 : " + event.getEventType());
			log.info("event값 : " + event.getPayload());
			assertThat(event.getProcessed()).isTrue();

		}

	}



}
