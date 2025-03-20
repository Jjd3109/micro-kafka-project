package micro.kafka.common.outboxmessagerelay;

import java.util.List;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutboxEventProcessor {

	private final OutboxEventRepository outboxEventRepository;
	private final KafkaTemplate<String, String> kafkaTemplate;

	@Scheduled(fixedRate = 5000) // 5초마다 실행
	@Transactional
	public void processOutboxEvents() {
		List<OutboxEvent> events = outboxEventRepository.findByProcessedFalse();

		for (OutboxEvent event : events) {
			try {
				// Kafka 전송
				kafkaTemplate.send(event.getEventType(), event.getAggregateId().toString(), event.getPayload());

				// 전송 성공 시 Outbox 상태 업데이트
				event.setProcessed(true);
				outboxEventRepository.save(event);


				log.info("Kafka 메시지 전송 성공");
			} catch (Exception e) {
				log.error("Kafka 메시지 전송 실패: {}", event, e);
			}
		}
	}
}
