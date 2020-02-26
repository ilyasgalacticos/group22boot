package kz.bitlab.springboot.group22.repositories;

import kz.bitlab.springboot.group22.entites.users.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {

    Users findByEmail(String email);

}
