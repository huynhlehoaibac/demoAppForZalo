package com.pa.demo.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import java.io.Serializable;

@NoRepositoryBean
public interface GenericRepository<T, K extends Serializable>
    extends JpaRepository<T, K>, JpaSpecificationExecutor<T> {
}
