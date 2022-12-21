package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJBDC implements SellerDao {

	private Connection connectionDb;

	public SellerDaoJBDC(Connection connectionDb) {
		this.connectionDb = connectionDb;
	}

	@Override
	public void insert(Seller object) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Seller object) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deletById(Seller id) {
		// TODO Auto-generated method stub

	}

	@Override
	public Seller findById(Integer id) {
		PreparedStatement prepStatement = null;
		ResultSet resultSet = null;

		try {
			prepStatement = connectionDb.prepareStatement(
							"SELECT seller.*,department.Name as DepName " 
							+ "FROM seller INNER JOIN department "
							+ "ON seller.DepartmentId = department.Id " 
							+ "WHERE seller.Id = ?");

			prepStatement.setInt(1, id);
			resultSet = prepStatement.executeQuery();

			if (resultSet.next()) {

				Department department = new Department();
				department.setId(resultSet.getInt("DepartmentId"));
				department.setName(resultSet.getString("DepName"));

				Seller obj = new Seller();
				obj.setId(resultSet.getInt("Id"));
				obj.setName(resultSet.getString("Name"));
				obj.setEmail(resultSet.getString("Email"));
				obj.setBirthDate(resultSet.getDate("BirthDate"));
				obj.setBaseSalary(resultSet.getDouble("BaseSalary"));
				obj.setDepartment(department);

				return obj;
			}
			return null;
		} catch (SQLException error) {
			throw new DbException(error.getMessage());
		} finally {
			DB.closeStatement(prepStatement);
			DB.closeResultSet(resultSet);
		}

	}

	@Override
	public List<Seller> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
