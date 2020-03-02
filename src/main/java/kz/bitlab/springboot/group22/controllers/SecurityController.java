package kz.bitlab.springboot.group22.controllers;

import kz.bitlab.springboot.group22.entites.Items;
import kz.bitlab.springboot.group22.entites.users.Users;
import kz.bitlab.springboot.group22.repositories.ItemsRepository;
import kz.bitlab.springboot.group22.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class SecurityController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemsRepository itemsRepository;

    @GetMapping(path = "/")
    public String index(Model model, @RequestParam(name = "page", defaultValue = "1", required = false) int page){

        model.addAttribute("user", getUserData());

        long size = itemsRepository.count();
        long tabSize = 5;
        long tabs = (size+tabSize-1)/tabSize;

        if(page<0){
            page = 1;
        }

        Pageable pageable = PageRequest.of(page-1, (int)tabSize);

        List<Items> items = itemsRepository.findAllByIdNotNullOrderByPriceDesc(pageable);

        model.addAttribute("items", items);
        model.addAttribute("tabs", tabs);

        return "home";
    }

    @GetMapping(path = "/login")
    public String login(Model model){
        return "login";
    }

    @GetMapping(path = "/additem")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String addItem(Model model){

        model.addAttribute("user", getUserData());

        return "additem";
    }

    @PostMapping(path = "/additem")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String addItem(@RequestParam(name = "name") String name,
                          @RequestParam(name = "price") int price){

        itemsRepository.save(new Items(null, name, price));
        return "redirect:/additem";

    }

    @GetMapping(path = "/details/{id}")
    public String details(Model model, @PathVariable(name = "id") Long id){
        Items item = itemsRepository.findById(id).get();
        model.addAttribute("item", item);
        model.addAttribute("user", getUserData());
        return "securitydetails";
    }

    @PostMapping(path = "/saveitem")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
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
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String deleteItem(@RequestParam(name = "id") Long id){

        Items item = itemsRepository.findById(id).get();
        itemsRepository.delete(item);

        return "redirect:/";

    }

    public Users getUserData(){
        Users user = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            User ud = (User) authentication.getPrincipal();
            user = userRepository.findByEmail(ud.getUsername());
        }
        return user;
    }


}
