//package nova.hackathon.util.ElasticSearch;
//
//import lombok.RequiredArgsConstructor;
//import nova.hackathon.community.entity.CommunityBoard;
//import nova.hackathon.community.repository.CommunityBoardRepository;
//import nova.hackathon.news.entity.News;
//import nova.hackathon.news.repository.NewsRepository;
//import nova.hackathon.notice.entity.Notice;
//import nova.hackathon.notice.repository.NoticeRepository;
//import nova.hackathon.util.ElasticSearch.Document.CommunityDocument;
//import nova.hackathon.util.ElasticSearch.Document.NewsDocument;
//import nova.hackathon.util.ElasticSearch.Document.NoticeDocument;
//import nova.hackathon.util.ElasticSearch.Document.SearchDocument;
//import nova.hackathon.util.ElasticSearch.Repository.CommunitySearchRepository;
//import nova.hackathon.util.ElasticSearch.Repository.NewsSearchRepository;
//import nova.hackathon.util.ElasticSearch.Repository.NoticeSearchRepository;
//import nova.hackathon.util.ElasticSearch.Repository.SearchRepository;
//import org.springframework.data.elasticsearch.core.SearchHit;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class CombinedSearchService {
//
////    private final CommunityBoardRepository communityBoardRepository;
////    private final CommunitySearchRepository communitySearchRepository;
//
////    private final SearchRepository searchRepository;
//
//    /**
//     * 모든 데이터를 Elasticsearch에 동기화
//     */
//    public void syncAll() {
//
///*        // Community
//        List<CommunityBoard> boards = communityBoardRepository.findAll();
//        List<CommunityDocument> communityDocuments = boards.stream()
//                .map(CommunityDocument::from)
//                .toList();
//        communitySearchRepository.saveAll(communityDocuments);*/
//    }
//
//    /**
//     * @param keyword 검색어
//     * @param type 필터 (notice / news / community)
//     * @param page 페이지 번호
//     * @param size 페이지 크기
//     * @return 하이라이트 포함된 검색 결과 리스트
//     */
//    public List<SearchResponseDTO> unifiedSearch(String keyword, String type, int page, int size) {
//        return searchRepository.search(keyword, type, page, size)
//                .getSearchHits().stream()
//                .map(searchHit -> convertToDTO((SearchHit<SearchDocument>) searchHit))
//                .collect(Collectors.toList());
//    }
//
//    /**
//     * SearchHit를 SearchResponseDTO로 변환
//     * @param searchHit SearchHit 객체
//     * @return 변환된 SearchResponseDTO
//     */
//    private SearchResponseDTO convertToDTO(SearchHit<SearchDocument> searchHit) {
//        String highlightedTitle = searchHit.getHighlightFields().get("title") != null ?
//                searchHit.getHighlightFields().get("title").get(0) : searchHit.getContent().getTitle();
//        String highlightedContent = searchHit.getHighlightFields().get("content") != null ?
//                searchHit.getHighlightFields().get("content").get(0) : searchHit.getContent().getContent();
//
//        return new SearchResponseDTO(
//                searchHit.getContent().getId(),
//                highlightedTitle,
//                highlightedContent,
//                searchHit.getContent().getDate(),
//                searchHit.getContent().getLink(),
//                searchHit.getContent().getCategory(),
//                searchHit.getContent().getType()
//        );
//    }
//}