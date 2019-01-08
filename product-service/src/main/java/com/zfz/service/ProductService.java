package com.zfz.service;

import com.zfz.service.bean.Product;

public class ProductService implements IProductService{

	public Product queryById(long id) {
		Product product = new Product();
		product.setId(id);
		product.setName("wait");
		product.setPrice(1.0);
		return product;
	}

}
