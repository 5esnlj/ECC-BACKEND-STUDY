@RestController   // Rest 컨트롤러임을 선언
public class ArticleApiController {
	@Autowired // 게시글 리파지토리 주입
	private ArticleRepository articleRepository

	// GET 요청 메서드 [모든 게시글 조회]
	@GetMapping("/api/articles")  // URL 요청 접수
	public List<Article> index() {  // index 메서드 정의 (article 묶음 반환)
		return articleRepository.findALL();
	}
	
	// GET 요청 메서드 [단일 게시글 조회]
	@GetMapping("/api/article/{id}")  
	public Article show(@PathVariable Long id) { 
		return articleRepository.findBy(id).orElse(Null);
  }

  
  // POST 요청 메서드
  @PostMapping("/api/articles")
  public Article create(@RequestBody ArticleForm dto) { // create 메서드 정의, 수정할 데이터는 dto 매개변수로 받아오기
	  Article article = dto.toEndity();  // 받아온 dto 엔티티로 변환
	  return articleRepository.save(article);  // articleRepository를 통해 DB에 저장후 반환
  }

  // PATCH 요청 메서드
  @PathchMapping("/api/article/{id}")
  public Article update(@PathVariable Long id,
											@RequestBody ArticleForm dto) {
  	// 1. DTO -> 엔티티 변환
  	Article article = dto.toEndity();  // 받아온 dto 엔티티로 변환
  	log.info("id: {}, article: {}", id, article.toString()); // 로그 찍기
  	
  	// 2. 타깃 조회
  	Article target = articleRepository.findByID(id).orElse(null);
  	
  	// 3. 잘못된 요청 처리
  	if (target == null || id != article.getID()) {  // 타깃이 null이거나 id가 다르면
  		// 400
  		log.info("400! id: {}, article: {}", id, article.toString()); // 오류 반환
		  return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
    
  	// 4. 업데이트 및 정상 응답 보내기
  	Article updated = articleRepository.save(article);  // 엔티티 DB에 저장
  	return ResponseEntity.status(HttpStatus.OK).body(updated);
  }	

  // DELETE 요청 메서드
  @DeleteMapping("/api/article/{id}")
  public ResponseEntity<Article> delete(@PathVariable Long id) {
  	// 대상 찾기
  	Article target = articleRepository.findByID(id).orElse(null);
  	
  	// 잘못된 요청 처리
  	if (target == null) {
  		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
  	}
	
  	// 대상 삭제
  	articleRepository.delete(target);
  	return ResponseEntity.status(HttpStatus.OK).body(updated);
  }

  
}
