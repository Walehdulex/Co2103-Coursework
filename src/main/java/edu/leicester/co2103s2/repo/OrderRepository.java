package edu.leicester.co2103s2.repo;


import edu.leicester.co2103s2.domain.OrderEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderRepository extends CrudRepository<OrderEntity, Long> {
    List<OrderEntity> findAll();
    List<OrderEntity> findByBooks_ISBN(String ISBN);
}
