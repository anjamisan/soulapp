package pmf.android.soulapp.ui;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

import pmf.android.soulapp.api.FoodListResponse;
import pmf.android.soulapp.api.NetworkCheck;
import pmf.android.soulapp.api.NutritionixApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FoodQueryActivity extends AppCompatActivity {
    
    private RecyclerView recyclerView;
    private FoodAdapter adapter;
    private List<FoodListResponse.Food> foodList; // List of food names
    private EditText searchField;
    Call<FoodListResponse> call; //salje http requests
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.myapplication.R.layout.food_query_layout);

        recyclerView = findViewById(R.id.recycler_view);
        searchField = findViewById(R.id.query_input);
        button = findViewById(R.id.button);
        foodList = new ArrayList<>();
        
        
        //RecyclerView i adapter
        adapter = new FoodAdapter(foodList, new OnItemClickListener() {
            @Override
            public void onItemClick(String foodName) {
                //pokrecem FoodFactsActivity
                Intent intent = new Intent(FoodQueryActivity.this, FoodFactsActivity.class);
                intent.putExtra("food_name", foodName);
                startActivity(intent);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        //nakon novog unosa pokreni Call
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = searchField.getText().toString().trim();
                if (!TextUtils.isEmpty(query)) {
                    Log.d("LISTENER", "Query: " + query);
                    searchFoodList(query);
                }
            }
        });
    }

    // Function to filter the food list based on the query (can be adjusted)
    private void searchFoodList(String query) {
        //provera konekcije
        if(NetworkCheck.isNetworkAvailable(getApplicationContext())){
            //Retrofit instanca
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://trackapi.nutritionix.com") // URL za Nutritionix API
                    .addConverterFactory(GsonConverterFactory.create()) // GSON za JSON konverziju
                    .build();

            // Create the Nutritionix API service
            NutritionixApi apiService = retrofit.create(NutritionixApi.class);

            // Call the getFoods method from the API and pass the query
            call = apiService.getFoods(query);

            // asynchronous execution da se ovo ne bi izvrsavalo na ui thread-u
            call.enqueue(new Callback<FoodListResponse>() {
                @Override
                public void onResponse(@NonNull Call<FoodListResponse> call, @NonNull Response<FoodListResponse> response) {
                    //FoodListResponse zamotan Response klasom radi lakse provere ispravnosti
                    if (response.isSuccessful() && response.body() != null) {
                        // Handle successful response and display data
                        FoodListResponse list = response.body();
                        List<FoodListResponse.Food> foods = list.getCommon();
                        runOnUiThread(() -> adapter.updateList(list.getCommon()));
                        Log.d("API_RESPONSE", "Fetched items: " + list.getCommon().size());
                    } else {
                        // ako neuspelo
                        Log.e("API_ERROR", "Response unsuccessful: " + response.message());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<FoodListResponse> call, @NonNull Throwable t) {
                    Log.e("API_ERROR", "Request failed: " + t.getMessage());
                }
            });
        }else{
            Toast.makeText(this, getString(R.string.no_conn), Toast.LENGTH_LONG).show();
        }
        
    }

    // RecyclerView Adapter
    private static class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {
        private List<FoodListResponse.Food> foodList;
        private OnItemClickListener listener;

        FoodAdapter(List<FoodListResponse.Food> foodList, OnItemClickListener listener) {
            this.foodList = foodList;
            this.listener = listener;
        }

        @NonNull
        @Override
        public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = View.inflate(parent.getContext(), R.layout.food_item_view, null);
            return new FoodViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
            String foodName = (foodList.get(position)).getFoodName();
            holder.foodNameTextView.setText(foodName);
            Log.d("BIND_VIEW", "Food instance: " + foodList.get(position).toString());
            //spoj listener
            holder.itemView.setOnClickListener(v -> listener.onItemClick(foodName));
        }

        @Override
        public int getItemCount() {
            return foodList.size();
        }

        public void updateList(List<FoodListResponse.Food> newFoodList) { //nakon svakog search-a
            Log.d("ADAPTER", "Updating list with " + newFoodList.size() + " items.");
            foodList.clear();
            foodList.addAll(newFoodList);
            Log.d("ADAPTER", "new foodlist " + getItemCount() + " items.");
            notifyDataSetChanged();
        }

        static class FoodViewHolder extends RecyclerView.ViewHolder {
            TextView foodNameTextView;

            FoodViewHolder(@NonNull View itemView) {
                super(itemView);
                foodNameTextView = itemView.findViewById(R.id.food_name);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (call != null) {
            call.cancel(); // obustavi http request ako se desava
        }
    }

}

