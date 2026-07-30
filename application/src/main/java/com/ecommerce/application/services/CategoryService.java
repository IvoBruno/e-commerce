package com.ecommerce.application.services;

import com.ecommerce.application.DTOs.CategoryDTO;
import com.ecommerce.application.entities.Category;
import com.ecommerce.application.repositories.CategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
   private final CategoryRepository categoryRepository;
   public CategoryService(CategoryRepository categoryRepository) {
      this.categoryRepository = categoryRepository;
   }

   @Transactional
   public List<CategoryDTO> findAll() {
      return categoryRepository.findAll()
              .stream()
              .map(CategoryDTO::new)
              .toList();
   }

   @Transactional
   public CategoryDTO getById(Long id) {
      return new  CategoryDTO(categoryRepository.getReferenceById(id));
   }

   @Transactional
   public CategoryDTO create(Category category) {
      return new CategoryDTO(categoryRepository.save(category));
   }

   @Transactional
   public CategoryDTO update(Category category) {
      return new CategoryDTO(categoryRepository.save(category));
   }

   @Transactional
   public void delete(Long id) {
      categoryRepository.deleteById(id);
   }


}
