package com.demo.jwtsecurity.service;

import com.demo.jwtsecurity.beans.ProductBean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    private List<ProductBean> productBeans;

    ProductService(){
            productBeans = new ArrayList<>();
            productBeans.add(new ProductBean("1", "Laptop", "50000", "Red"));
            productBeans.add(new ProductBean("2", "TV", "25000", "Green"));
            productBeans.add(new ProductBean("3", "Fridge", "21000", "Black"));
    }

    public List<ProductBean> findAll(){
        return productBeans;
    }

    public ProductBean save(ProductBean bean){
        if ( null!=bean ){
            productBeans.add(bean);
            return bean;
        }
        return null;
    }

    public ProductBean findById(String id){
        for (ProductBean bean : productBeans){
            if ( id.equals(bean.getId()) ){
                return bean;
            }
        }
        return null;
    }

    public boolean deleteById(String id){
        ProductBean bean = findById(id);
        if (null!=bean){
            productBeans.remove(bean);
            return true;
        }
        return false;
    }

    public ProductBean updateById(String id, ProductBean bean) {
        for ( ProductBean oldBean: productBeans  ){
            if ( id.equalsIgnoreCase(oldBean.getId()) ){
                oldBean.setName(bean.getName());
                oldBean.setColor(bean.getColor());
                oldBean.setCost(bean.getCost());
                return oldBean;
            }
        }
        return null;
    }
}
