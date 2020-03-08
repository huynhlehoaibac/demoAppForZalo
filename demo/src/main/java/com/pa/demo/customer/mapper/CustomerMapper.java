package com.pa.demo.customer.mapper;

import org.springframework.stereotype.Component;

import com.pa.demo.common.entity.Customer;
import com.pa.demo.common.mapper.Mapper;
import com.pa.demo.customer.dto.CustomerDTO;

@Component
public class CustomerMapper implements Mapper<Customer, CustomerDTO> {

  @Override
  public Customer toEntity(CustomerDTO source) {

    if (source == null) {
      return null;
    }

    // @formatter:off
    return Customer.builder()
        .customerId(source.getCustomerId())
        .customerName(source.getCustomerName())
        .customerType(source.getCustomerType())
        .balance(source.getBalance())
        .phone(source.getPhone())
        .email(source.getEmail())
        .address(source.getAddress())
        .status(source.isStatus())
        .accountNumber(source.getAccountNumber())
        .gender(source.getGender())
        .updateDate(source.getUpdateDate())
        .insertDate(source.getInsertDate())
        .build();
    // @formatter:on
  }

  @Override
  public CustomerDTO toDTO(Customer source) {

    if (source == null) {
      return null;
    }

    // @formatter:off
    return CustomerDTO.builder()
        .customerName(source.getCustomerName())
        .customerType(source.getCustomerType())
        .balance(source.getBalance())
        .phone(source.getPhone())
        .email(source.getEmail())
        .address(source.getAddress())
        .status(source.isStatus())
        .accountNumber(source.getAccountNumber())
        .gender(source.getGender())
        .build();
    // @formatter:on
  }
}

