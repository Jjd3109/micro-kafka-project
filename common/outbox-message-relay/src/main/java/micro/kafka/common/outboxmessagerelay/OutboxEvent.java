package micro.kafka.common.outboxmessagerelay;

import java.time.LocalDateTime;

import com.google.gson.Gson;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "outbox_event")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
public class OutboxEvent {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String eventType;

	@Lob
	private String payload; // JSON 형태로 저장

	private Long aggregateId;

	private LocalDateTime createdAt;

	private Boolean processed;

	public static OutboxEvent create(String eventType, Object payload, Long aggregateId) {
		return OutboxEvent.builder()
			.eventType(eventType)
			.payload(GsonUtil.getGson().toJson(payload)) // GsonUtil 사용
			.aggregateId(aggregateId)
			.createdAt(LocalDateTime.now())
			.processed(false)
			.build();
	}
}
