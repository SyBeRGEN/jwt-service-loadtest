package com.example.authservice.user_auth.repositories;

import com.example.authservice.user_auth.dto.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {
}
