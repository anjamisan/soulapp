package pmf.android.soulapp.api;

import java.util.List;

public class FoodListResponse {

    private List<Food> common;

    public List<Food> getCommon() {
        return common;
    }

    public void setCommon(List<Food> common) {
        this.common = common;
    }

    public static class Food {
        private String food_name;

        public String getFoodName() {
            return food_name;
        }

        public void setFoodName(String foodName) {
            this.food_name = foodName;
        }

        @Override
        public String toString() {
            return "foodName='" + food_name;
        }
    }
}
