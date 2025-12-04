package com.sparta.msa.example_part_3.domain.user.repository;

import com.sparta.msa.example_part_3.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
