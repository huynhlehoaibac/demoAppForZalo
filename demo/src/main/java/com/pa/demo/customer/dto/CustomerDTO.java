package com.pa.demo.customer.dto;

import java.math.BigDecimal;
import java.util.Date;

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
public class CustomerDTO {

	int customerId;
	String customerName;
	String customerType;
	BigDecimal balance;
	String phone;
	String email;
	String address;
	boolean status;
	String accountNumber;
	String gender;
	Date updateDate;
	Date insertDate;
}
