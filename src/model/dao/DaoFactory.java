package model.dao;

import model.dao.impl.SellerDaoJBDC;

public class DaoFactory {

	public static SellerDao creatSellerDao() {

		return new SellerDaoJBDC();
	}

}
