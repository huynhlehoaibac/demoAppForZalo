package com.pa.demo.customer.model;

import java.math.BigDecimal;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
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
public class UpdateCustomerModel {
  @NotBlank(message = "This field is required.")
  @Length(max = 256, message = "This field must be no more than {1} characters long.")
  String customerName;

  @Length(max = 1, message = "This field must be no more than {1} characters long.")
  @NotBlank(message = "This field is required.")
  String customerType;

  @Digits(integer = 14, fraction = 2,
      message = "This field must be a number with maximum of {1} digits and {2} decimal places.")
  BigDecimal balance;

  @Length(max = 16, message = "This field must be no more than {1} characters long.")
  @NotBlank(message = "This field is required.")
  String phone;

  @Length(max = 256, message = "This field must be no more than {1} characters long.")
  String email;

  @Length(max = 256, message = "This field must be no more than {1} characters long.")
  String address;

  Boolean status;

  @Length(max = 256, message = "This field must be no more than {1} characters long.")
  String accountNumber;

  @Length(max = 1, message = "This field must be no more than {1} characters long.")
  String gender;
}
