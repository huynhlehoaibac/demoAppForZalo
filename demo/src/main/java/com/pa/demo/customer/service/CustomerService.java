package com.pa.demo.customer.service;

import java.io.IOException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.itextpdf.text.DocumentException;
import com.pa.demo.customer.dto.CustomerDTO;
import com.pa.demo.customer.model.CreateCustomerModel;
import com.pa.demo.customer.model.ExportCustomerModel;
import com.pa.demo.customer.model.SearchCustomerModel;
import com.pa.demo.customer.model.UpdateCustomerModel;

public interface CustomerService {

  Page<CustomerDTO> searchCustomers(SearchCustomerModel searchCustomerModel, Pageable pageable);

  byte[] exportCustomers(ExportCustomerModel exportCriteriaCustomerModel, Pageable pageable)
      throws IOException, DocumentException;

  CustomerDTO getCustomer(Integer customerId);

  void createCustomer(CreateCustomerModel createCustomerModel);

  void updateCustomer(Integer customerId, UpdateCustomerModel updateCustomerModel);

  void deleteCustomer(Integer customerId);

}
