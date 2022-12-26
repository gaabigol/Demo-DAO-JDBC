package model.dao;

import db.DB;
import model.dao.impl.DepartmentJDBC;
import model.dao.impl.SellerDaoJBDC;

public class DaoFactory {

	public static SellerDao creatSellerDao() {

		return new SellerDaoJBDC(DB.getConnection());
	}
	
	public static DepartmentDao creatDepartmentDao() {
		
		return new DepartmentJDBC(DB.getConnection());
	}

}
