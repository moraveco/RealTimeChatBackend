package com.moraveco.springboot.repository;

import com.moraveco.springboot.entity.Login;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LoginRepository extends JpaRepository<Login, String> {
    boolean existsByEmail(String email);
    Login findByEmail(String email);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO mydb.users (id, name, mydb.users.lastname) " +
            "VALUES (:id, :name, :lastname )", nativeQuery = true)
    void saveUser(
            @Param("id") String id,
            @Param("name") String name,
            @Param("lastname") String lastname
    );

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO mydb.login (mydb.login.email, mydb.login.password) " +
            "VALUES (:email, :password )", nativeQuery = true)
    void saveLogin(
            @Param("email") String email,
            @Param("password") String password
    );


}
