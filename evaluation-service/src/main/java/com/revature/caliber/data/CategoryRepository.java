package com.revature.caliber.data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.revature.caliber.beans.Category;

/**
 * Spring Data operations for the type {@link Category}
 * 
 * @author Patrick Walsh
 *
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

	/**
	 * Find active or inactive categories. Orders them by categoryId so the most
	 * common can be placed first
	 * 
	 * @return categories
	 */
	public List<Category> findByActiveOrderByCategoryIdAsc(Boolean active);

	/**
	 * Find all categories and sort them alphabetically
	 * 
	 * @return categories
	 */
	public List<Category> findAll();
	
	/**
	 * Find a category by the given identifier
	 * 
	 * @return category
	 */
	public Category findOne(Integer id);
	
	/**
	 * Save a new category to the database
	 * 
	 * @param category
	 * @return updated category
	 */
	@SuppressWarnings("unchecked")
	public Category save(Category category);

	/**
	 * Find a Category by the string name, such as "Java"
	 * 
	 * @param skillCategory
	 * @return category
	 */
	public Category findBySkillCategory(String skillCategory);
	
}
