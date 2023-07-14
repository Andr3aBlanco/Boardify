package com.boardify.boardify.repository;

import com.boardify.boardify.entities.GameCategory;
import jdk.jfr.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface GameCategoryRepository extends JpaRepository<GameCategory, String> {
}
