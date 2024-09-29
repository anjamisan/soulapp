package pmf.android.soulapp.ui;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;

import java.util.List;

import okhttp3.RequestBody;
import pmf.android.soulapp.api.FoodFactsQuery;
import pmf.android.soulapp.api.FoodFactsResponse;
import pmf.android.soulapp.api.NutritionixApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FoodFactsActivity extends AppCompatActivity {

    private ImageView foodImage;
    private TextView foodName, servingQty, servingUnit, calories, grams, totalFat, satFat, cholesterol, sodium, totalCarbs, fiber, sugars, protein;
    private String food_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_facts_layout);

        //hrana koja je odabrana
        Intent intent = getIntent();
        food_name = intent.getStringExtra("food_name");

        //
        foodImage = findViewById(com.example.myapplication.R.id.food_image);
        foodName = findViewById(R.id.food_name);
        servingQty = findViewById(R.id.serving_qty);
        servingUnit = findViewById(R.id.serving_unit);
        calories = findViewById(R.id.calories);
        grams = findViewById(R.id.grams);
        totalFat = findViewById(R.id.tot_fat);
        satFat = findViewById(R.id.sat_fat);
        cholesterol = findViewById(R.id.cholesterol);
        sodium = findViewById(R.id.sodium);
        totalCarbs = findViewById(R.id.tot_carbohydrate);
        fiber = findViewById(R.id.fiber);
        sugars = findViewById(R.id.sugars);
        protein = findViewById(R.id.protein);

        // Initialize Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://trackapi.nutritionix.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NutritionixApi api = retrofit.create(NutritionixApi.class);

        // pravim request
        FoodFactsQuery query = new FoodFactsQuery(food_name);

        // Make the POST call
        Call<FoodFactsResponse> call = api.getNutrients(query);
        call.enqueue(new Callback<FoodFactsResponse>() {
            @Override
            public void onResponse(@NonNull Call<FoodFactsResponse> call, @NonNull Response<FoodFactsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("API_RESPONSE_JSON", response.body().toString());

                    extractResponse(response.body());
                } else {
                    // ako neuspelo
                    Log.e("API_ERROR", "Response unsuccessful: " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<FoodFactsResponse> call, Throwable t) {
                Log.e("API_ERROR", "Request failed: " + t.getMessage());
            }
        });


    }

    private void extractResponse(FoodFactsResponse foods){
        runOnUiThread(() -> {
            FoodFactsResponse.FoodFacts foodFacts = foods.getFoods().get(0);

            foodName.setText(String.format("%s: %s", getString(R.string.food_name), foodFacts.getFood_name()));
            servingQty.setText(String.format("%s: %d", getString(R.string.serving_qty), foodFacts.getServing_qty()));
            servingUnit.setText(String.format("%s: %s", getString(R.string.serving_unit), foodFacts.getServing_unit()));
            grams.setText(String.format("%s: %.2f%s", getString(R.string.grams), foodFacts.getServing_weight_grams(), getString(R.string.g)));
            calories.setText(String.format("%s: %.2f%s", getString(R.string.calories), foodFacts.getNf_calories(), getString(R.string.kcal)));
            totalFat.setText(String.format("%s: %.2f%s", getString(R.string.tot_fat), foodFacts.getNf_total_fat(), getString(R.string.g)));
            satFat.setText(String.format("%s: %.2f%s", getString(R.string.sat_fat), foodFacts.getNf_saturated_fat(), getString(R.string.g)));
            cholesterol.setText(String.format("%s: %.2f%s", getString(R.string.cholesterol), foodFacts.getNf_cholesterol(), getString(R.string.mg)));
            sodium.setText(String.format("%s: %.2f%s", getString(R.string.sodium), foodFacts.getNf_sodium(), getString(R.string.mg)));
            totalCarbs.setText(String.format("%s: %.2f%s", getString(R.string.sodium), foodFacts.getNf_total_carbohydrate(), getString(R.string.g)));
            fiber.setText(String.format("%s: %.2f%s", getString(R.string.fiber), foodFacts.getNf_dietary_fiber(), getString(R.string.g)));
            sugars.setText(String.format("%s: %.2f%s", getString(R.string.sugars), foodFacts.getNf_sugars(), getString(R.string.g)));
            protein.setText(String.format("%s: %.2f%s", getString(R.string.protein), foodFacts.getNf_protein(), getString(R.string.g)));

            // ucitavanje slike
            if(foodFacts.getPhoto() != null){Glide.with(this)
                    .load(foodFacts.getPhoto().getThumb())  // ucitava se url
                    .placeholder(R.drawable.nutritionix)  // dok se ucitava
                    .into(foodImage);}
        });
    }
}