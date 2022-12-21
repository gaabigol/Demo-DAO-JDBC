package model.dao;

import db.DB;
import model.dao.impl.SellerDaoJBDC;

public class DaoFactory {

	public static SellerDao creatSellerDao() {

		return new SellerDaoJBDC(DB.getConnection());
	}

}
