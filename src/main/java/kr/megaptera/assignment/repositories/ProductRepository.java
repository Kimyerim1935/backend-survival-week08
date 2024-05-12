package kr.megaptera.assignment.repositories;

import java.util.List;

import kr.megaptera.assignment.models.Product;
import kr.megaptera.assignment.models.ProductId;
import org.springframework.data.repository.CrudRepository;


public interface ProductRepository extends CrudRepository<Product, ProductId> {
    List<Product> findAll();
}
