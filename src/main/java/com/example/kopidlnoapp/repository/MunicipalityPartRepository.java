package com.example.kopidlnoapp.repository;

import com.example.kopidlnoapp.entity.MunicipalityPart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MunicipalityPartRepository extends JpaRepository<MunicipalityPart, String> {
}