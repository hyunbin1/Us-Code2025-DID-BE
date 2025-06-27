package nova.hackathon.util.ElasticSearch.EventSynchronization;

import lombok.Data;
import nova.hackathon.util.ElasticSearch.Document.SearchDocument;

@Data
public class EntityIndexEvent<T extends SearchDocument> {
    private final T document;
    private final IndexAction action;

    public enum IndexAction { INSERT, UPDATE, DELETE }
    public EntityIndexEvent(T document, IndexAction action) {
        this.document = document;
        this.action = action;
    }
}