package seekers;

public class AverageResponse {

    private final long id;
    private final String content;

    public AverageResponse(long id, String content) {
        this.id = id;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
}