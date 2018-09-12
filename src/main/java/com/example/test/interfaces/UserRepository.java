package com.example.test.interfaces;

import com.example.test.pojo.Appuser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Appuser, Long> {
}
