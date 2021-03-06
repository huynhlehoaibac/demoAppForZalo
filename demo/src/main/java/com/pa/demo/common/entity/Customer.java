package com.pa.demo.common.entity;
// Generated Mar 6, 2020 01:52:02 PM by Hibernate Tools 5.2.10.Final

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Builder;

/**
 * customer generated by hbm2java
 */
@Builder
@Entity
@Table(name = "customer")
public class Customer implements java.io.Serializable {
  private static final long serialVersionUID = 1L;

  private int customerId;
  private String customerName;
  private String customerType;
  private BigDecimal balance;
  private String phone;
  private String email;
  private String address;
  private Boolean status;
  private String accountNumber;
  private String gender;
  private Date updateDate;
  private Date insertDate;

  public Customer() {}

  public Customer(int customerId, String customerName, String customerType, BigDecimal balance,
      String phone, String email, String address, boolean status, String accountNumber,
      String gender, Date updateDate, Date insertDate) {
    this.customerName = customerName;
    this.customerType = customerType;
    this.balance = balance;
    this.phone = phone;
    this.email = email;
    this.address = address;
    this.status = status;
    this.accountNumber = accountNumber;
    this.gender = gender;
    this.updateDate = updateDate;
    this.insertDate = insertDate;
  }

  @Id

  @Column(name = "customer_id", unique = true, nullable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  public int getCustomerId() {
    return this.customerId;
  }

  public void setCustomerId(int customerId) {
    this.customerId = customerId;
  }

  @Column(name = "customer_name", nullable = false)
  public String getCustomerName() {
    return this.customerName;
  }

  public void setCustomerName(String customerName) {
    this.customerName = customerName;
  }

  @Column(name = "customer_type", nullable = false)
  public String getCustomerType() {
    return this.customerType;
  }

  public void setCustomerType(String customerType) {
    this.customerType = customerType;
  }

  @Column(name = "balance")
  public BigDecimal getBalance() {
    return this.balance;
  }

  public void setBalance(BigDecimal balance) {
    this.balance = balance;
  }

  @Column(name = "phone", nullable = false)
  public String getPhone() {
    return this.phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  @Column(name = "email", nullable = false)
  public String getEmail() {
    return this.email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @Column(name = "address")
  public String getAddress() {
    return this.address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  @Column(name = "status")
  public Boolean getStatus() {
    return this.status;
  }

  public void setStatus(Boolean status) {
    this.status = status;
  }

  @Column(name = "account_number")
  public String getAccountNumber() {
    return this.accountNumber;
  }

  public void setAccountNumber(String accountNumber) {
    this.accountNumber = accountNumber;
  }

  @Column(name = "gender")
  public String getGender() {
    return this.gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "update_date", length = 29)
  public Date getUpdateDate() {
    return this.updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "insert_date", nullable = false, length = 29)
  public Date getInsertDate() {
    return this.insertDate;
  }

  public void setInsertDate(Date insertDate) {
    this.insertDate = insertDate;
  }
}
