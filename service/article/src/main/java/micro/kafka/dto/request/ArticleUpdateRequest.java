package micro.kafka.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class ArticleUpdateRequest {
	private String title;
	private String content;
}
