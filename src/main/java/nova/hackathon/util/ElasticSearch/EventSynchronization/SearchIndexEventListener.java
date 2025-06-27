//package nova.hackathon.util.ElasticSearch.EventSynchronization;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import nova.hackathon.util.ElasticSearch.Document.CommunityDocument;
//import nova.hackathon.util.ElasticSearch.Document.NewsDocument;
//import nova.hackathon.util.ElasticSearch.Document.NoticeDocument;
//import nova.hackathon.util.ElasticSearch.Document.SearchDocument;
//import nova.hackathon.util.ElasticSearch.Repository.CommunitySearchRepository;
//import nova.hackathon.util.ElasticSearch.Repository.NewsSearchRepository;
//import nova.hackathon.util.ElasticSearch.Repository.NoticeSearchRepository;
//import org.springframework.context.event.EventListener;
//import org.springframework.stereotype.Component;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class SearchIndexEventListener {
//    private final NoticeSearchRepository noticeSearchRepository;
//    private final NewsSearchRepository newsSearchRepository;
//    private final CommunitySearchRepository communitySearchRepository;
//
//
//
//    @EventListener
//    public void handleEntityIndexEvent(EntityIndexEvent<? extends SearchDocument> event) {
//        SearchDocument doc = event.getDocument();
//        try {
//            switch (event.getAction()) {
//                case INSERT, UPDATE -> {
//                    if (doc instanceof NoticeDocument notice) {
//                        noticeSearchRepository.save(notice);
//                    } else if (doc instanceof NewsDocument news) {
//                        newsSearchRepository.save(news);
//                    } else if (doc instanceof CommunityDocument comm) {
//                        communitySearchRepository.save(comm);
//                    }
//                    log.info("[Elasticsearch] [{}] 문서 {} 처리 성공 (ID: {})",
//                            doc.getType(), event.getAction(), doc.getId());
//                }
//
//                case DELETE -> {
//                    if (doc instanceof NoticeDocument notice) {
//                        noticeSearchRepository.deleteById(notice.getId());
//                    } else if (doc instanceof NewsDocument news) {
//                        newsSearchRepository.deleteById(news.getId());
//                    } else if (doc instanceof CommunityDocument comm) {
//                        communitySearchRepository.deleteById(comm.getId());
//                    }
//                    log.info("[Elasticsearch] [{}] 문서 삭제 성공 (ID: {})", doc.getType(), doc.getId());
//                }
//            }
//
//        } catch (Exception e) {
//            log.error("[Elasticsearch] [{}] 문서 {} 처리 실패 (ID: {}) - {}",
//                    doc.getType(), event.getAction(), doc.getId(), e.getMessage(), e);
//        }
//    }
//}
