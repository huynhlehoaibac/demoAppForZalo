package com.pa.demo.customer.controller;

import java.io.IOException;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.itextpdf.text.DocumentException;
import com.pa.demo.customer.dto.CustomerDTO;
import com.pa.demo.customer.model.CreateCustomerModel;
import com.pa.demo.customer.model.ExportCustomerModel;
import com.pa.demo.customer.model.SearchCustomerModel;
import com.pa.demo.customer.model.UpdateCustomerModel;
import com.pa.demo.customer.service.CustomerService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/customers")
public class CustomerController {

  @Autowired
  CustomerService service;

  @GetMapping(value = {"", "/"})
  public Page<CustomerDTO> searchCustomers(SearchCustomerModel searchCustomerModel,
      Pageable pageable) {
    return service.searchCustomers(searchCustomerModel, pageable);
  }

  @GetMapping(value = {"", "/"}, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
  public byte[] excelExportCustomers(ExportCustomerModel exportCustomerModel,
      Pageable pageable) throws IOException, DocumentException {
    return service.exportCustomers(exportCustomerModel, pageable);
  } 

  @GetMapping(value = {"/{customerId}"})
  public CustomerDTO getCustomer(@PathVariable("customerId") Integer customerId) {
    return service.getCustomer(customerId);
  }

  @PostMapping({"", "/"})
  public void createCustomer(@Valid @RequestBody CreateCustomerModel createCustomerModel) {
    service.createCustomer(createCustomerModel);
  }

  @PatchMapping({"/{customerId}"})
  public void updateCustomer(@PathVariable("customerId") Integer customerId,
      @Valid @RequestBody UpdateCustomerModel updateCustomerModel) {
    service.updateCustomer(customerId, updateCustomerModel);
  }

  @DeleteMapping({"/{customerId}"})
  public void deleteCustomer(@PathVariable("customerId") Integer customerId) {
    service.deleteCustomer(customerId);
  }
}
