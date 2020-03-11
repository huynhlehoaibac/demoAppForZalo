package com.pa.demo.customer.model;

import java.math.BigDecimal;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExportCustomerModel {

  String customerName;
  String customerType;
  BigDecimal balance;
  String phone;
  String email;
  String address;
  Boolean status;
  String accountNumber;
  String gender;

  List<String> displayedColumnFields;
  String fileType;
}
