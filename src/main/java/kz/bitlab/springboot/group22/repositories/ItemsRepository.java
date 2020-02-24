package kz.bitlab.springboot.group22.repositories;

import kz.bitlab.springboot.group22.entites.Items;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemsRepository extends JpaRepository<Items, Long> {

    Items findByName(String name);
    List<Items> findAllByNameLikeOrderByPriceDesc(String name);

}
