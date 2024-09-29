package pmf.android.soulapp.api;

public class FoodFactsQuery {
    private String query;

    public FoodFactsQuery(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}