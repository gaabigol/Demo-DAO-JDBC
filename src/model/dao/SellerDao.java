package model.dao;

import java.util.List;

import model.entities.Seller;

public interface SellerDao {


	void insert(Seller object);

	void update(Seller object);

	void deletById(Seller id);

	Seller findById(Integer id);

	List<Seller> findAll();

}
