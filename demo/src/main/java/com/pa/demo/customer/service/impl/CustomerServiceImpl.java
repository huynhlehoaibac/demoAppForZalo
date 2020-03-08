package com.pa.demo.customer.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pa.demo.common.repository.CustomerRepository;
import com.pa.demo.customer.mapper.CustomerMapper;
import com.pa.demo.customer.service.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	CustomerRepository organizationRepository;

	@Autowired
	CustomerMapper organizationMapper;

}
