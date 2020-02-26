package kz.bitlab.springboot.group22.controllers;

import kz.bitlab.springboot.group22.entites.Items;
import kz.bitlab.springboot.group22.repositories.ItemsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(path = "/archive")
public class MainController {

    @Autowired
    private ItemsRepository itemsRepository;

    @GetMapping(path = "/")
    public String index(Model model, @RequestParam(name = "key", defaultValue = "") String key){

        List<Items> items;

        if(key.equals("")){
            items = itemsRepository.findAll();
        }else{
            items = itemsRepository.findAllByNameLikeOrderByPriceDesc("%"+key+"%");
        }

        model.addAttribute("items", items);

        return "index";
    }

    @PostMapping(path = "/additem")
    public String addItem(@RequestParam(name = "name") String name,
                          @RequestParam(name = "price") int price){

        itemsRepository.save(new Items(null, name, price));
        return "redirect:/";

    }

    @GetMapping(path = "/details/{id}")
    public String details(Model model, @PathVariable(name = "id") Long id){
        Items item = itemsRepository.findById(id).get();
        model.addAttribute("item", item);
        return "details";
    }

    @PostMapping(path = "/saveitem")
    public String saveItem(@RequestParam(name = "name") String name,
                           @RequestParam(name = "price") int price,
                           @RequestParam(name = "id") Long id){

        Items item = itemsRepository.findById(id).get();
        item.setName(name);
        item.setPrice(price);

        itemsRepository.save(item);
        return "redirect:/details/"+id;

    }

    @PostMapping(path = "/delete")
    public String deleteItem(@RequestParam(name = "id") Long id){

        Items item = itemsRepository.findById(id).get();
        itemsRepository.delete(item);

        return "redirect:/";

    }
}
