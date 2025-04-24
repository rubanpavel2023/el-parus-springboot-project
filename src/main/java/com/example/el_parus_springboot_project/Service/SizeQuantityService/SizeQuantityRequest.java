package com.example.el_parus_springboot_project.Service.SizeQuantityService;


import java.util.List;

public class SizeQuantityRequest {
        private String article;
        private String category;
        private List<SizeQuantityDto> sizes;

        public String getArticle() {
            return article;
        }

        public void setArticle(String article) {
            this.article = article;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public List<SizeQuantityDto> getSizes() {
            return sizes;
        }

        public void setSizes(List<SizeQuantityDto> sizes) {
            this.sizes = sizes;
        }
    }


