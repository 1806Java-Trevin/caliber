package com.revature.caliber.test.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.revature.caliber.Application;
import com.revature.caliber.CaliberTest;
import com.revature.caliber.beans.Address;
import com.revature.caliber.data.AddressRepository;

public class AddressRepositoryTest extends CaliberTest{

	private static final Logger log = Logger.getLogger(AddressRepositoryTest.class);
	private static final String ADDRESS_COUNT = "select count(address_id) from caliber_address";

	@Autowired
	private AddressRepository addressRepository;


	//Tests the save method in AddressRepository. Creates a new address and checks if the address appears, and then checks to make sure the size has increased.
	@Test
	public void saveAddressRepository(){
		log.debug("Saving a new Address using AddressRepository");
		int before = jdbcTemplate.queryForObject(ADDRESS_COUNT, Integer.class);
		Address address = new Address(1, "Sunshine st","New Hope", "MN", "55428", "moneybags inc",false);
		addressRepository.save(address);
		int after = jdbcTemplate.queryForObject(ADDRESS_COUNT, Integer.class);
		int addressId = address.getAddressId();
		assertEquals(address, addressRepository.findOne(addressId));
		assertEquals(++before, after);
	}
	//Tests the gettAll method in AddressRepository and returns a String version of it. Asserts that is is not empty, the size is correct, and the findAll string version is equal to getAll.
	@Test
	public void getAllAddressRepository(){
		log.debug("Getting all addresses using AddressRepository getAll function");
		int num = jdbcTemplate.queryForObject(ADDRESS_COUNT, Integer.class);
		assertNotNull(addressRepository.findAll());
		assertEquals(addressRepository.findAll().size(), num);
	}

	//Tests the findAll method in AddressRepository and returns a list. Asserts the findAll is not empty and that it is the correct size.
	@Test
	public void findAllAddressRepository(){
		log.debug("Find All Addresses");
		int size = jdbcTemplate.queryForObject(ADDRESS_COUNT, Integer.class);
		assertFalse(addressRepository.findAll().isEmpty());
		assertEquals(addressRepository.findAll().size(),size);
	}

	//Tests to make sure an address not in the database returns null. 
	@Test
	public void nullGetAddressByInt(){
		log.debug("About to fail gettingAddressByInt");
		Address address = addressRepository.findOne(9999999);
		assertNull(address);
	}

	//Tests update method of AddressRepository. Asserts that addresses zipcode was changed.
	@Test
	public void updateAddressRepository(){
		log.debug("UpdateAddressRepository Test");
		String zipcode = "11111";
		Address address = addressRepository.findOne(2);
		address.setZipcode(zipcode);
		addressRepository.save(address);
		assertEquals(zipcode, addressRepository.findOne(2).getZipcode());
	}
}