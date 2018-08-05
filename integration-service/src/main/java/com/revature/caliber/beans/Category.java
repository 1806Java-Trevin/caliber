package com.revature.caliber.beans;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The type Category.
 */
public class Category implements Serializable {

	private static final long serialVersionUID = 3363756954535297728L;

	@JsonProperty(value = "categoryId")
	private int categoryId;

	@JsonProperty(value = "skillCategory")
	private String skillCategory;
	
	@JsonProperty(value = "active")
	private boolean active;

	@JsonIgnore
	private Set<Assessment> assessments;

	/**
	 * Instantiates a new Category.
	 */
	public Category() {
		super();
	}

	/**
	 * Create new category
	 * @param skillCategory
	 * @param active
	 */
	public Category(String skillCategory, boolean active) {
		super();
		this.skillCategory = skillCategory;
		this.active = active;
	}

	/**
	 * Gets category id.
	 *
	 * @return the category id
	 */
	public int getCategoryId() {
		return categoryId;
	}

	/**
	 * Sets category id.
	 *
	 * @param categoryId
	 *            the category id
	 */
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	/**
	 * Gets skill category.
	 *
	 * @return the skill category
	 */
	public String getSkillCategory() {
		return skillCategory;
	}

	/**
	 * Sets skill category.
	 *
	 * @param skillCategory
	 *            the skill category
	 */
	public void setSkillCategory(String skillCategory) {
		this.skillCategory = skillCategory;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * Gets assessments.
	 *
	 * @return the assessments
	 */
	public Set<Assessment> getAssessments() {
		return assessments;
	}

	/**
	 * Sets assessments.
	 *
	 * @param assessments
	 *            the assessments
	 */
	public void setAssessments(Set<Assessment> assessments) {
		this.assessments = assessments;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (active ? 1231 : 1237);
		result = prime * result + ((skillCategory == null) ? 0 : skillCategory.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Category other = (Category) obj;
		if (active != other.active)
			return false;
		if (skillCategory == null) {
			if (other.skillCategory != null)
				return false;
		} else if (!skillCategory.equals(other.skillCategory))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return skillCategory;
	}
	
	public static class SkillCategoryAscendingComparator implements Comparator<Category> {

		@Override
		public int compare(Category here, Category there) {
			return here.getSkillCategory().compareTo(there.getSkillCategory());
		}
		
	}

}