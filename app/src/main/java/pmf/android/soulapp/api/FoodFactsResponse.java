package pmf.android.soulapp.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FoodFactsResponse {

    private List<FoodFacts> foods;  // Use a list to hold food items

    public List<FoodFacts> getFoods() {
        return foods;
    }

    public void setFoods(List<FoodFacts> foods) {
        this.foods = foods;
    }
    //
    public static class FoodFacts{
        @SerializedName("food_name")
        private String food_name;

        @SerializedName("serving_unit")
        private String serving_unit;

        @SerializedName("serving_qty")
        private int serving_qty;

        @SerializedName("serving_weight_grams")
        private double serving_weight_grams;

        @SerializedName("nf_calories")
        private double nf_calories;

        @SerializedName("nf_total_fat")
        private double nf_total_fat;

        @SerializedName("nf_saturated_fat")
        private double nf_saturated_fat;

        @SerializedName("nf_cholesterol")
        private double nf_cholesterol;

        @SerializedName("nf_sodium")
        private double nf_sodium;

        @SerializedName("nf_total_carbohydrate")
        private double nf_total_carbohydrate;

        @SerializedName("nf_dietary_fiber")
        private double nf_dietary_fiber;

        @SerializedName("nf_sugars")
        private double nf_sugars;

        @SerializedName("nf_protein")
        private double nf_protein;

        @SerializedName("photo")
        private Photo photo;


        public FoodFacts() {}

        public FoodFacts(String food_name, double nf_calories, double nf_cholesterol, double nf_dietary_fiber, double nf_protein, double nf_saturated_fat, double nf_sodium, double nf_sugars, double nf_total_carbohydrate, double nf_total_fat, Photo photo, int serving_qty, String serving_unit, double serving_weight_grams) {
            this.food_name = food_name;
            this.nf_calories = nf_calories;
            this.nf_cholesterol = nf_cholesterol;
            this.nf_dietary_fiber = nf_dietary_fiber;
            this.nf_protein = nf_protein;
            this.nf_saturated_fat = nf_saturated_fat;
            this.nf_sodium = nf_sodium;
            this.nf_sugars = nf_sugars;
            this.nf_total_carbohydrate = nf_total_carbohydrate;
            this.nf_total_fat = nf_total_fat;
            this.photo = photo;
            this.serving_qty = serving_qty;
            this.serving_unit = serving_unit;
            this.serving_weight_grams = serving_weight_grams;
        }

        public String getFood_name() {
            return food_name;
        }

        public void setFood_name(String food_name) {
            this.food_name = food_name;
        }

        public double getNf_calories() {
            return nf_calories;
        }

        public void setNf_calories(double nf_calories) {
            this.nf_calories = nf_calories;
        }

        public double getNf_cholesterol() {
            return nf_cholesterol;
        }

        public void setNf_cholesterol(double nf_cholesterol) {
            this.nf_cholesterol = nf_cholesterol;
        }

        public double getNf_dietary_fiber() {
            return nf_dietary_fiber;
        }

        public void setNf_dietary_fiber(double nf_dietary_fiber) {
            this.nf_dietary_fiber = nf_dietary_fiber;
        }

        public double getNf_protein() {
            return nf_protein;
        }

        public void setNf_protein(double nf_protein) {
            this.nf_protein = nf_protein;
        }

        public double getNf_saturated_fat() {
            return nf_saturated_fat;
        }

        public void setNf_saturated_fat(double nf_saturated_fat) {
            this.nf_saturated_fat = nf_saturated_fat;
        }

        public double getNf_sodium() {
            return nf_sodium;
        }

        public void setNf_sodium(double nf_sodium) {
            this.nf_sodium = nf_sodium;
        }

        public double getNf_sugars() {
            return nf_sugars;
        }

        public void setNf_sugars(double nf_sugars) {
            this.nf_sugars = nf_sugars;
        }

        public double getNf_total_carbohydrate() {
            return nf_total_carbohydrate;
        }

        public void setNf_total_carbohydrate(double nf_total_carbohydrate) {
            this.nf_total_carbohydrate = nf_total_carbohydrate;
        }

        public double getNf_total_fat() {
            return nf_total_fat;
        }

        public void setNf_total_fat(double nf_total_fat) {
            this.nf_total_fat = nf_total_fat;
        }

        public Photo getPhoto() {
            return photo;
        }

        public void setPhoto(Photo photo) {
            this.photo = photo;
        }

        public int getServing_qty() {
            return serving_qty;
        }

        public void setServing_qty(int serving_qty) {
            this.serving_qty = serving_qty;
        }

        public String getServing_unit() {
            return serving_unit;
        }

        public void setServing_unit(String serving_unit) {
            this.serving_unit = serving_unit;
        }

        public double getServing_weight_grams() {
            return serving_weight_grams;
        }

        public void setServing_weight_grams(double serving_weight_grams) {
            this.serving_weight_grams = serving_weight_grams;
        }


        public static class Photo {
            private String thumb;

            public String getThumb() {
                return thumb;
            }

            public void setThumb(String thumb) {
                this.thumb = thumb;
            }

        }
    }



}

