package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Statement;

import db.DB;
import db.DbException;
import db.DbIntegrityException;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentJDBC implements DepartmentDao {

	private Connection connectionDb;

	public DepartmentJDBC(Connection connection) {
		this.connectionDb = connection;
	}

	@Override
	public void insert(Department object) {
		PreparedStatement prepStatement = null;
		try {
			prepStatement = connectionDb.prepareStatement(
					"INSERT INTO department "
					+ "(Name) "
					+ "VALUES "
					+ "(?) ", Statement.RETURN_GENERATED_KEYS);
			
			prepStatement.setString(1, object.getName());
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
	public void update(Department object) {
		PreparedStatement prepStatment = null ;
		
		try {
			prepStatment = connectionDb.prepareStatement(
					"UPDATE department "
					+ "SET Name = ? "
					+ "WHERE Id = ? ");
			
			prepStatment.setString(1, object.getName());
			prepStatment.setInt(2, object.getId());
			
			prepStatment.executeUpdate();
			
		} catch (SQLException error) {
			throw new DbException(error.getMessage());
		} finally {
			DB.closeStatement(prepStatment);
		}
	}

	@Override
	public void deletById(Integer id) {
		PreparedStatement st = null;
		try {
			st = connectionDb.prepareStatement(
				"DELETE FROM department WHERE Id = ?");

			st.setInt(1, id);

			st.executeUpdate();
		}
		catch (SQLException e) {
			throw new DbIntegrityException(e.getMessage());
		} 
		finally {
			DB.closeStatement(st);
		}
	}

	

	@Override
	public Department findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = connectionDb.prepareStatement(
				"SELECT * FROM department WHERE Id = ?");
			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Department obj = new Department();
				obj.setId(rs.getInt("Id"));
				obj.setName(rs.getString("Name"));
				return obj;
			}
			return null;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Department> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = connectionDb.prepareStatement(
				"SELECT * FROM department ORDER BY Name");
			rs = st.executeQuery();

			List<Department> list = new ArrayList<>();

			while (rs.next()) {
				Department obj = new Department();
				obj.setId(rs.getInt("Id"));
				obj.setName(rs.getString("Name"));
				list.add(obj);
			}
			return list;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}


}
