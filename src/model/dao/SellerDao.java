package model.dao;

import java.util.List;

import model.entities.Department;
import model.entities.Seller;

public interface SellerDao {

	void insert(Seller object);

	void update(Seller object);

	void deletById(Integer id);

	Seller findById(Integer id);

	List<Seller> findAll();

	List<Seller> findByDepartment(Department department);
}
