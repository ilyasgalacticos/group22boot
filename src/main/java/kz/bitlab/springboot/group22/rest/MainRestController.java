package kz.bitlab.springboot.group22.rest;

import kz.bitlab.springboot.group22.entites.Items;
import kz.bitlab.springboot.group22.repositories.ItemsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/rest")
public class MainRestController {

    @Autowired
    private ItemsRepository itemsRepository;

    @ResponseBody
    @GetMapping(path = "/allitems")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<List<Items>> getAllItems(){
        List<Items> allItems = itemsRepository.findAll();
        return new ResponseEntity<>(allItems, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping(path = "/additem")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<String> addItem(@RequestParam(name = "name") String name,
                                          @RequestParam(name = "price") int price){

        Items item = new Items(null, name, price);
        itemsRepository.save(item);

        return new ResponseEntity<>("Item added successfully", HttpStatus.OK);

    }

    @ResponseBody
    @PostMapping(path = "/delete")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<String> deleteItem(@RequestParam(name = "id") Long id){

        Items item = itemsRepository.getOne(id);
        itemsRepository.delete(item);

        return new ResponseEntity<>("Item deleted!!!", HttpStatus.OK);

    }

}