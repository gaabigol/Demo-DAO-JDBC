package application;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.dao.impl.SellerDaoJBDC;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {

		SellerDao sellerDao = DaoFactory.creatSellerDao();

		System.out.println("======= Test:1 ======== Seller FindByID");
		Seller seller = sellerDao.findById(3);
		System.out.println(seller);

		System.out.println("\n ====== Test:2 ========== Seller FindByDepartment");
		Department department = new Department(2, null);
		List<Seller> list = sellerDao.findByDepartment(department);
		for (Seller obj : list) {
			System.out.println(obj);
		}

		System.out.println("\n ======== Test: 3 ========= Seller FindALL");
		List<Seller> listall = sellerDao.findAll();
		for (Seller allSeller : listall) {
			System.out.println(allSeller);
		}
	}

}
