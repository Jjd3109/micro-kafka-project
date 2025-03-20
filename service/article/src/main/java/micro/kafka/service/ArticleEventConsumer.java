package micro.kafka.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ArticleEventConsumer {

	@KafkaListener(topics = "article-created", groupId = "article-group")
	public void consumeArticleCreated(ConsumerRecord<String, String> record) {
		log.info("🔥 Kafka 메시지 수신: key={}, value={}", record.key(), record.value());

		// JSON을 객체로 변환할 경우 사용
		// ArticleCreatedEvent event = objectMapper.readValue(record.value(), ArticleCreatedEvent.class);

		// 처리 로직 추가 (DB 저장, 캐시 업데이트 등)
	}
}