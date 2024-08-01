package com.example.kopidlnoapp.repository;

import com.example.kopidlnoapp.entity.Municipality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MunicipalityRepository extends JpaRepository<Municipality, String> {
}