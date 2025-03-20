package micro.kafka.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import micro.kafka.dto.request.ArticleCreateRequest;
import micro.kafka.dto.response.ArticleResponse;
import micro.kafka.service.ArticleService;

@RestController
@RequiredArgsConstructor
@Log4j2
public class ArticleController {

	private final ArticleService articleService;

	@PostMapping("/v1/create")
	public ArticleResponse create(@RequestBody ArticleCreateRequest articleCreateRequest){
		return articleService.create(articleCreateRequest);
	}

}
