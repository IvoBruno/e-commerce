package com.ecommerce.application.services;

import com.ecommerce.application.DTOs.CategoryDTO;
import com.ecommerce.application.entities.Category;
import com.ecommerce.application.repositories.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@AllArgsConstructor
public class CategoryService {
   private final CategoryRepository categoryRepository;

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
   public CategoryDTO create(CategoryDTO category) {
      return new CategoryDTO(categoryRepository.save(Category.builder()
              .name(category.name())
              .description(category.description())
              .build()));
   }

   @Transactional
   public CategoryDTO update(Long id, CategoryDTO category) {
      Category obj = categoryRepository.getReferenceById(id);
      obj.setName(category.name());
      obj.setDescription(category.description());
      Category objUpdated = categoryRepository.save(obj);
      return new CategoryDTO(objUpdated);
   }

   @Transactional
   public void delete(Long id) {
      categoryRepository.deleteById(id);
   }


}
