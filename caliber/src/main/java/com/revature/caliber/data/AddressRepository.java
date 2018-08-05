package com.revature.caliber.data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.revature.caliber.beans.Address;

/**
 * Spring Data operations for the type {@link Address}
 * @author Emily Higgins
 *
 */
@Repository
public interface AddressRepository extends JpaRepository<Address, Integer>{
	
	/**
	 * Save an address to the database
	 *
	 * @param address
	 * @return updated address
	 */
	@SuppressWarnings("unchecked")
	public Address save(Address address);

	/**
	 * @return a list of all addresses
	 */
	public List<Address> findAll();
	
	/**
	 * Find address with the specified id
	 * 
	 * @param id
	 * @return address
	 */
	public Address findOne(int id);
	
}
