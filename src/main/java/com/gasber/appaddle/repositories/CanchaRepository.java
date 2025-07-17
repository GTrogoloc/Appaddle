package com.gasber.appaddle.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.gasber.appaddle.models.Cancha;

@Repository
public interface CanchaRepository  extends JpaRepository<Cancha, Long>{
    
}
