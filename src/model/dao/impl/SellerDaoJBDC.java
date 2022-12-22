package model.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		PreparedStatement prepStatement = null;

		try {

			prepStatement = connectionDb.prepareStatement(
					"INSERT INTO Seller "
					+ "(Name, Email, BirthDate, BaseSalary, DepartmentId) " 
					+ "VALUES " 
					+ "(?, ?, ?, ?, ?) ",
					Statement.RETURN_GENERATED_KEYS);

			prepStatement.setString(1, object.getName());
			prepStatement.setString(2, object.getEmail());
			prepStatement.setDate(3, new Date(object.getBirthDate().getTime()));
			prepStatement.setDouble(4, object.getBaseSalary());
			prepStatement.setInt(5, object.getDepartment().getId());

			int rowsAffected = prepStatement.executeUpdate();

			if (rowsAffected > 0) {
				ResultSet resultSet = prepStatement.getGeneratedKeys();
				if (resultSet.next()) {

					int id = resultSet.getInt(1);
					object.setId(id);
				}

				DB.closeResultSet(resultSet);

			} else {
				throw new DbException("Unexpected error! No rows affected!");
			}
		} catch (SQLException error) {
			throw new DbException(error.getMessage());
		} finally {
			DB.closeStatement(prepStatement);
		}

	}

	@Override
	public void update(Seller object) {
		PreparedStatement prepStatement = null;

		try {

			prepStatement = connectionDb.prepareStatement(
					"UPDATE seller "
					+ "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? "
					+ "WHERE Id = ?");

			prepStatement.setString(1, object.getName());
			prepStatement.setString(2, object.getEmail());
			prepStatement.setDate(3, new Date(object.getBirthDate().getTime()));
			prepStatement.setDouble(4, object.getBaseSalary());
			prepStatement.setInt(5, object.getDepartment().getId());
			prepStatement.setInt(6, object.getId());

			prepStatement.executeUpdate();

		} catch (SQLException error) {
			throw new DbException(error.getMessage());
		} finally {
			DB.closeStatement(prepStatement);
		}
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

				Department department = instatiateDepartment(resultSet);
				Seller obj = instantiateSeller(resultSet, department);
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

	private Seller instantiateSeller(ResultSet resultSet, Department department) throws SQLException {
		Seller obj = new Seller();
		obj.setId(resultSet.getInt("Id"));
		obj.setName(resultSet.getString("Name"));
		obj.setEmail(resultSet.getString("Email"));
		obj.setBirthDate(resultSet.getDate("BirthDate"));
		obj.setBaseSalary(resultSet.getDouble("BaseSalary"));
		obj.setDepartment(department);

		return obj;
	}

	private Department instatiateDepartment(ResultSet resultSet) throws SQLException {
		Department department = new Department();
		department.setId(resultSet.getInt("DepartmentId"));
		department.setName(resultSet.getString("DepName"));

		return department;
	}

	@Override
	public List<Seller> findAll() {

		PreparedStatement prepStatement = null;
		ResultSet resultSet = null;

		try {
			prepStatement = connectionDb.prepareStatement(
					"SELECT seller.*,department.Name as DepName " 
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id " 
					+ "ORDER BY Name");

			resultSet = prepStatement.executeQuery();

			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();

			while (resultSet.next()) {
				Department department = map.get(resultSet.getInt("DepartmentId"));

				if (department == null) {
					department = instatiateDepartment(resultSet);
					map.put(resultSet.getInt("DepartmentId"), department);
				}

				Seller obj = instantiateSeller(resultSet, department);
				list.add(obj);
			}
			return list;

		} catch (SQLException error) {
			throw new DbException(error.getMessage());
		} finally {
			DB.closeStatement(prepStatement);
			DB.closeResultSet(resultSet);
		}
	}

	@Override
	public List<Seller> findByDepartment(Department department) {
		PreparedStatement prepStatement = null;
		ResultSet resultSet = null;

		try {
			prepStatement = connectionDb.prepareStatement(
					"SELECT seller.*,department.Name as DepName " 
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id " 
					+ "WHERE DepartmentId = ? " 
					+ "ORDER BY Name");

			prepStatement.setInt(1, department.getId());
			resultSet = prepStatement.executeQuery();

			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();

			while (resultSet.next()) {
				Department dep = map.get(resultSet.getInt("DepartmentId"));
				if (dep == null) {
					dep = instatiateDepartment(resultSet);
					map.put(resultSet.getInt("DepartmentId"), dep);
				}

				Seller obj = instantiateSeller(resultSet, dep);
				list.add(obj);
			}
			return list;

		} catch (SQLException error) {
			throw new DbException(error.getMessage());
		} finally {
			DB.closeStatement(prepStatement);
			DB.closeResultSet(resultSet);
		}

	}

}
