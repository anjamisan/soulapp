package pmf.android.soulapp.api;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface NutritionixApi {

    // Static header-i
    @Headers({
            "x-app-id: 700511ad",
            "x-app-key: 0e0b20902d0ec61e9e6f3bbd83427478",
            "common: true",
            "branded: false"
    })
    @GET("/v2/search/instant")
    Call<FoodListResponse> getFoods(
            @Query("query") String query  // ovu metodu prvo, dobijamo predloge za hranu
    );

    //post
    @Headers({
            "Content-Type: application/json",
            "x-app-id: 700511ad",
            "x-app-key: 0e0b20902d0ec61e9e6f3bbd83427478"
    })
    @POST("/v2/natural/nutrients")
    Call<FoodFactsResponse> getNutrients(
            @Body FoodFactsQuery body  // kad odabere hranu pravi se body i prosledjuje u ovu metodu
    );
}
