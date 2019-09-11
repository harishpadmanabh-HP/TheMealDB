package com.hp.themealdb.Models;

import java.util.List;

public class FilterByArea {

    private List<MealsBean> meals;

    public List<MealsBean> getMeals() {
        return meals;
    }

    public void setMeals(List<MealsBean> meals) {
        this.meals = meals;
    }

    public static class MealsBean {
        /**
         * strMeal : BeaverTails
         * strMealThumb : https://www.themealdb.com/images/media/meals/ryppsv1511815505.jpg
         * idMeal : 52928
         */

        private String strMeal;
        private String strMealThumb;
        private String idMeal;

        public String getStrMeal() {
            return strMeal;
        }

        public void setStrMeal(String strMeal) {
            this.strMeal = strMeal;
        }

        public String getStrMealThumb() {
            return strMealThumb;
        }

        public void setStrMealThumb(String strMealThumb) {
            this.strMealThumb = strMealThumb;
        }

        public String getIdMeal() {
            return idMeal;
        }

        public void setIdMeal(String idMeal) {
            this.idMeal = idMeal;
        }
    }
}
