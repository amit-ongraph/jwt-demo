package com.demo.jwtsecurity.controller;

import com.demo.jwtsecurity.beans.ProductBean;
import com.demo.jwtsecurity.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    ProductService service;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ProductBean> findAll() {
        return service.findAll();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ProductBean save(@RequestBody ProductBean bean) {
        return service.save(bean);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ProductBean findById(@PathVariable("id") String id) {
        return service.findById(id);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public boolean deleteById(@PathVariable("id") String id) {
        return service.deleteById(id);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ProductBean updateById(@PathVariable("id") String id, @RequestBody ProductBean bean) {
        return service.updateById(id, bean);
    }


}
