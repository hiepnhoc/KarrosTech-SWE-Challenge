package com.example.demo.dao;

import com.example.demo.model.UserGpx;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface GpxRepo extends JpaRepository<UserGpx, Long> {

    @Query(value = "SELECT * FROM GPX where username=:username", nativeQuery = true)
    Page<UserGpx> findAllByUsername(@Param("username") String username, Pageable pageable);
}
