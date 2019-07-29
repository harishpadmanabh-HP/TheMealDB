package com.hp.themealdb.Models;

import java.util.List;

public class CategoryModel {

    private List<CategoriesBean> categories;

    public List<CategoriesBean> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoriesBean> categories) {
        this.categories = categories;
    }

    public static class CategoriesBean {
        /**
         * idCategory : 1
         * strCategory : Beef
         * strCategoryThumb : https://www.themealdb.com/images/category/beef.png
         * strCategoryDescription : Beef is the culinary name for meat from cattle, particularly skeletal muscle. Humans have been eating beef since prehistoric times.[1] Beef is a source of high-quality protein and essential nutrients.[2]
         */

        private String idCategory;
        private String strCategory;
        private String strCategoryThumb;
        private String strCategoryDescription;

        public String getIdCategory() {
            return idCategory;
        }

        public void setIdCategory(String idCategory) {
            this.idCategory = idCategory;
        }

        public String getStrCategory() {
            return strCategory;
        }

        public void setStrCategory(String strCategory) {
            this.strCategory = strCategory;
        }

        public String getStrCategoryThumb() {
            return strCategoryThumb;
        }

        public void setStrCategoryThumb(String strCategoryThumb) {
            this.strCategoryThumb = strCategoryThumb;
        }

        public String getStrCategoryDescription() {
            return strCategoryDescription;
        }

        public void setStrCategoryDescription(String strCategoryDescription) {
            this.strCategoryDescription = strCategoryDescription;
        }
    }
}
