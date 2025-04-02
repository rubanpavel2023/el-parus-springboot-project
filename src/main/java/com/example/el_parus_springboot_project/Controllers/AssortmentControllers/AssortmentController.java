package com.example.el_parus_springboot_project.Controllers.AssortmentControllers;

import com.example.el_parus_springboot_project.Entity.Goods;
import com.example.el_parus_springboot_project.Repositories.GoodsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class AssortmentController {

    @GetMapping("/title")
    public String titlePage() {
        return "title";
    }

    @GetMapping("/assortmentGucci")
    public String assortmentGucci() {
        return "assortmentGucci";
    }

    @GetMapping("/assortmentChanel")
    public String assortmentChanel() {
        return "assortmentChanel";
    }

    @GetMapping("/assortmentMKors")
    public String assortmentMKors() {
        return "assortmentMKors";
    }

    @GetMapping("/assortmentBurberry")
    public String assortmentBurberry() {
        return "assortmentBurberry";
    }


}


