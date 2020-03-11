package com.pa.demo.customer.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.Predicate;
import org.apache.poi.ooxml.POIXMLProperties;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.pa.demo.common.constant.Constant;
import com.pa.demo.common.entity.Customer;
import com.pa.demo.common.enums.CustomerTypeEnum;
import com.pa.demo.common.enums.GenderEnum;
import com.pa.demo.common.repository.CustomerRepository;
import com.pa.demo.common.service.MessageSource;
import com.pa.demo.common.util.Util;
import com.pa.demo.customer.dto.CustomerDTO;
import com.pa.demo.customer.mapper.CustomerMapper;
import com.pa.demo.customer.model.CreateCustomerModel;
import com.pa.demo.customer.model.ExportCustomerModel;
import com.pa.demo.customer.model.SearchCustomerModel;
import com.pa.demo.customer.model.UpdateCustomerModel;
import com.pa.demo.customer.service.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService {

  private static Map<String, String> headerMap = new HashMap<>();
  static {
    headerMap.put("customerName", "Name");
    headerMap.put("customerType", "Type");
    headerMap.put("balance", "Balance");
    headerMap.put("phone", "Phone");
    headerMap.put("email", "Email");
    headerMap.put("address", "Address");
    headerMap.put("status", "Status");
    headerMap.put("accountNumber", "Account No");
    headerMap.put("gender", "Gender");
  }

  @Autowired
  CustomerRepository customerRepository;
  @Autowired
  CustomerMapper customerMapper;
  @Autowired
  private MessageSource messageSource;

  @Override
  public Page<CustomerDTO> searchCustomers(SearchCustomerModel searchCustomerModel,
      Pageable pageable) {
    return customerRepository.findAll(searchCustomersSpecs(searchCustomerModel), pageable)
        .map(customerMapper::toDTO);
  }

  @Override
  public byte[] exportCustomers(ExportCustomerModel exportCustomerModel, Pageable pageable)
      throws IOException, DocumentException {
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    switch (exportCustomerModel.getFileType()) {
      case "pdf":
        pdfExport(exportCustomerModel, pageable, os);
        break;
      case "xlsx":
        excelExport(exportCustomerModel, pageable, os);
      default:
        break;
    }
    return os.toByteArray();
  }

  private void excelExport(ExportCustomerModel exportCustomerModel, Pageable pageable,
      ByteArrayOutputStream os) throws IOException {
    try (Workbook workbook = new XSSFWorkbook()) {

      // <!-- Set properties.
      POIXMLProperties props = ((XSSFWorkbook) workbook).getProperties();

      POIXMLProperties.CoreProperties coreProps = props.getCoreProperties();
      coreProps.setCreator("Huynh Le Hoai Bac");
      coreProps.setCreated(Optional.of(new Date()));
      coreProps.setSubjectProperty("Customer");
      // Set properties. -->

      CreationHelper createHelper = workbook.getCreationHelper();
      DataFormat dataFormat = createHelper.createDataFormat();
      CellStyle textCellStyle = workbook.createCellStyle();
      textCellStyle.setDataFormat(dataFormat.getFormat("text"));

      List<String> displayedColumnFields = exportCustomerModel.getDisplayedColumnFields();
      if (displayedColumnFields == null) {
        displayedColumnFields = new ArrayList<>();
      }

      //// Sheet
      Sheet sheet = workbook.createSheet(messageSource.getMessage("Services"));

      List<String> columns = new ArrayList<>();
      for (String field : displayedColumnFields) {
        if (headerMap.containsKey(field)) {
          columns.add(headerMap.get(field));
        }
      }

      int headerRowIndex = 0;
      prepareHeaderRow(sheet, headerRowIndex, columns.toArray(new String[0]));

      // Obtains service data
      List<Customer> customers =
          customerRepository.findAll(exportCustomerModelsSpecs(exportCustomerModel),
              PageRequest.of(0, Integer.MAX_VALUE, pageable.getSort())).getContent();

      int baseDataRowIndex = headerRowIndex;
      prepareCustomerDataRow(sheet, baseDataRowIndex, customers, displayedColumnFields);

      workbook.write(os);
    }
  }

  private void pdfExport(ExportCustomerModel exportCustomerModel, Pageable pageable,
      ByteArrayOutputStream os) throws DocumentException {
    Document document = new Document(PageSize.A4, 25, 25, 25, 25);
    PdfWriter writer = PdfWriter.getInstance(document, os);

    document.open();
    Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
    Chunk chunk = new Chunk("This feature is under development", font);

    document.add(chunk);
    document.close();
  }

  @Override
  public CustomerDTO getCustomer(Integer customerId) {
    Customer customer =
        customerRepository.findById(customerId).orElseThrow(() -> new EntityNotFoundException(
            MessageFormatter.format(Constant.CUSTOMER_ID_NOT_FOUND, customerId).getMessage()));
    return customerMapper.toDTO(customer);
  }

  @Transactional
  @Override
  public void createCustomer(CreateCustomerModel createCustomerModel) {
    Customer customer = new Customer();

    // Set inserting information.
    customer.setInsertDate(new Date());

    // Set basic information.
    customer.setCustomerName(createCustomerModel.getCustomerName());

    CustomerTypeEnum customerTypeEnum =
        CustomerTypeEnum.getEnum(createCustomerModel.getCustomerType());
    if (customerTypeEnum != null) {
      customer.setCustomerType(customerTypeEnum.getCode());
    }

    customer.setBalance(createCustomerModel.getBalance());
    customer.setPhone(createCustomerModel.getPhone());
    customer.setEmail(createCustomerModel.getEmail());
    customer.setAddress(createCustomerModel.getAddress());
    customer.setStatus(createCustomerModel.getStatus());
    customer.setAccountNumber(createCustomerModel.getAccountNumber());

    GenderEnum genderEnum = GenderEnum.getEnum(createCustomerModel.getGender());
    if (genderEnum != null) {
      customer.setGender(genderEnum.getCode());
    }
    customer.setGender(createCustomerModel.getGender());

    customerRepository.save(customer);
  }

  @Transactional
  @Override
  public void updateCustomer(Integer customerId, UpdateCustomerModel updateCustomerModel) {
    Customer customer =
        customerRepository.findById(customerId).orElseThrow(() -> new EntityNotFoundException(
            MessageFormatter.format(Constant.CUSTOMER_ID_NOT_FOUND, customerId).getMessage()));

    // Set updating information.
    customer.setUpdateDate(new Date());

    // Set basic information.
    customer.setCustomerName(updateCustomerModel.getCustomerName());

    CustomerTypeEnum customerTypeEnum =
        CustomerTypeEnum.getEnum(updateCustomerModel.getCustomerType());
    if (customerTypeEnum != null) {
      customer.setCustomerType(customerTypeEnum.getCode());
    }

    customer.setBalance(updateCustomerModel.getBalance());
    customer.setPhone(updateCustomerModel.getPhone());
    customer.setEmail(updateCustomerModel.getEmail());
    customer.setAddress(updateCustomerModel.getAddress());
    customer.setStatus(updateCustomerModel.getStatus());
    customer.setAccountNumber(updateCustomerModel.getAccountNumber());

    GenderEnum genderEnum = GenderEnum.getEnum(updateCustomerModel.getGender());
    if (genderEnum != null) {
      customer.setGender(genderEnum.getCode());
    }
    customer.setGender(updateCustomerModel.getGender());

    customerRepository.save(customer);
  }

  @Transactional
  @Override
  public void deleteCustomer(Integer customerId) {
    Customer customer =
        customerRepository.findById(customerId).orElseThrow(() -> new EntityNotFoundException(
            MessageFormatter.format(Constant.CUSTOMER_ID_NOT_FOUND, customerId).getMessage()));
    customerRepository.delete(customer);
  }

  private Specification<Customer> exportCustomerModelsSpecs(
      ExportCustomerModel exportCustomerModelModel) {
    return (root, query, cb) -> {

      List<Predicate> predicates = new ArrayList<>();

      if (!StringUtils.isEmpty(exportCustomerModelModel.getCustomerName())) {
        predicates.add(cb.like(cb.lower(root.get("customerName")),
            Util.simpleFullLikeExpression(exportCustomerModelModel.getCustomerName(), false)));
      }

      if (!StringUtils.isEmpty(exportCustomerModelModel.getCustomerType())) {
        predicates
            .add(cb.equal(root.get("customerType"), exportCustomerModelModel.getCustomerType()));
      }

      if (exportCustomerModelModel.getBalance() != null) {
        predicates.add(cb.gt(root.get("balance"), exportCustomerModelModel.getBalance()));
      }

      if (!StringUtils.isEmpty(exportCustomerModelModel.getPhone())) {
        predicates.add(cb.like(cb.lower(root.get("phone")),
            Util.simpleFullLikeExpression(exportCustomerModelModel.getPhone(), false)));
      }

      if (!StringUtils.isEmpty(exportCustomerModelModel.getEmail())) {
        predicates.add(cb.like(cb.lower(root.get("email")),
            Util.simpleFullLikeExpression(exportCustomerModelModel.getEmail(), false)));
      }

      if (!StringUtils.isEmpty(exportCustomerModelModel.getAddress())) {
        predicates.add(cb.like(cb.lower(root.get("address")),
            Util.simpleFullLikeExpression(exportCustomerModelModel.getAddress(), false)));
      }

      if (exportCustomerModelModel.getStatus() != null) {
        predicates.add(cb.equal(root.get("status"), exportCustomerModelModel.getStatus()));
      }

      if (!StringUtils.isEmpty(exportCustomerModelModel.getAccountNumber())) {
        predicates.add(cb.like(cb.lower(root.get("accountNumber")),
            Util.simpleFullLikeExpression(exportCustomerModelModel.getAccountNumber(), false)));
      }

      if (!StringUtils.isEmpty(exportCustomerModelModel.getGender())) {
        predicates.add(cb.equal(root.get("gender"), exportCustomerModelModel.getGender()));
      }

      return cb.and(predicates.toArray(new Predicate[0]));
    };
  }

  private Specification<Customer> searchCustomersSpecs(SearchCustomerModel searchCustomerModel) {
    return (root, query, cb) -> {

      List<Predicate> predicates = new ArrayList<>();

      if (!StringUtils.isEmpty(searchCustomerModel.getCustomerName())) {
        predicates.add(cb.like(cb.lower(root.get("customerName")),
            Util.simpleFullLikeExpression(searchCustomerModel.getCustomerName(), false)));
      }

      if (!StringUtils.isEmpty(searchCustomerModel.getCustomerType())) {
        predicates.add(cb.equal(root.get("customerType"), searchCustomerModel.getCustomerType()));
      }

      if (searchCustomerModel.getBalance() != null) {
        predicates.add(cb.gt(root.get("balance"), searchCustomerModel.getBalance()));
      }

      if (!StringUtils.isEmpty(searchCustomerModel.getPhone())) {
        predicates.add(cb.like(cb.lower(root.get("phone")),
            Util.simpleFullLikeExpression(searchCustomerModel.getPhone(), false)));
      }

      if (!StringUtils.isEmpty(searchCustomerModel.getEmail())) {
        predicates.add(cb.like(cb.lower(root.get("email")),
            Util.simpleFullLikeExpression(searchCustomerModel.getEmail(), false)));
      }

      if (!StringUtils.isEmpty(searchCustomerModel.getAddress())) {
        predicates.add(cb.like(cb.lower(root.get("address")),
            Util.simpleFullLikeExpression(searchCustomerModel.getAddress(), false)));
      }

      if (searchCustomerModel.getStatus() != null) {
        predicates.add(cb.equal(root.get("status"), searchCustomerModel.getStatus()));
      }

      if (!StringUtils.isEmpty(searchCustomerModel.getAccountNumber())) {
        predicates.add(cb.like(cb.lower(root.get("accountNumber")),
            Util.simpleFullLikeExpression(searchCustomerModel.getAccountNumber(), false)));
      }

      if (!StringUtils.isEmpty(searchCustomerModel.getGender())) {
        predicates.add(cb.equal(root.get("gender"), searchCustomerModel.getGender()));
      }

      return cb.and(predicates.toArray(new Predicate[0]));
    };
  }

  /**
   * prepare header row
   * 
   * @param headerRow
   * @param rowKeys
   */
  private void prepareHeaderRow(Sheet sheet, int headerRowIndex, String[] rowKeys) {
    Row headerRow = sheet.createRow(headerRowIndex);
    for (int i = 0; i < rowKeys.length; i++) {
      Cell cell = headerRow.createCell(i);
      cell.setCellValue(messageSource.getMessage(rowKeys[i]));
    }
  }

  /**
   * prepare customer data rows
   * 
   * @param sheet
   * @param baseRowIndex
   * @param customers
   * @param dislayedColumnFields
   */
  private void prepareCustomerDataRow(Sheet sheet, int baseRowIndex, List<Customer> customers,
      List<String> dislayedColumnFields) {
    Row dataRow = null;
    int colIndex = -1;
    Cell cell = null;

    for (Customer customer : customers) {
      dataRow = sheet.createRow(++baseRowIndex);
      colIndex = -1;

      if (dislayedColumnFields.contains("customerName")) {
        cell = dataRow.createCell(++colIndex);
        CellUtil.setCellStyleProperty(cell, CellUtil.DATA_FORMAT,
            BuiltinFormats.getBuiltinFormat("text"));
        cell.setCellValue(customer.getCustomerName());
      }

      if (dislayedColumnFields.contains("customerType")) {
        cell = dataRow.createCell(++colIndex);
        CellUtil.setCellStyleProperty(cell, CellUtil.DATA_FORMAT,
            BuiltinFormats.getBuiltinFormat("text"));
        String customerTypeText = null;
        switch (customer.getCustomerType()) {
          case "A":
            customerTypeText = "Vip";
            break;
          case "B":
            customerTypeText = "Regular";
            break;
          case "C":
            customerTypeText = "Standard";
            break;
          case "D":
            customerTypeText = "New";
            break;
          default:
            break;
        }
        cell.setCellValue(messageSource.getMessage(customerTypeText));
      }

      if (dislayedColumnFields.contains("balance")) {
        cell = dataRow.createCell(++colIndex);
        if (customer.getBalance() != null) {
          CellUtil.setCellStyleProperty(cell, CellUtil.DATA_FORMAT,
              BuiltinFormats.getBuiltinFormat("\"#,##0.00\""));
          cell.setCellValue(customer.getBalance().doubleValue());
        }
      }

      if (dislayedColumnFields.contains("phone")) {
        cell = dataRow.createCell(++colIndex);
        CellUtil.setCellStyleProperty(cell, CellUtil.DATA_FORMAT,
            BuiltinFormats.getBuiltinFormat("text"));
        cell.setCellValue(customer.getPhone());
      }

      if (dislayedColumnFields.contains("email")) {
        cell = dataRow.createCell(++colIndex);
        CellUtil.setCellStyleProperty(cell, CellUtil.DATA_FORMAT,
            BuiltinFormats.getBuiltinFormat("text"));
        cell.setCellValue(customer.getEmail());
      }

      if (dislayedColumnFields.contains("address")) {
        cell = dataRow.createCell(++colIndex);
        CellUtil.setCellStyleProperty(cell, CellUtil.DATA_FORMAT,
            BuiltinFormats.getBuiltinFormat("text"));
        cell.setCellValue(customer.getAddress());
      }

      if (dislayedColumnFields.contains("status")) {
        String statusText = null;
        if (customer.getStatus() != null) {
          String statusCode = customer.getStatus() ? "Active" : "Blocked";
          statusText = messageSource.getMessage(statusCode);
        }
        cell = dataRow.createCell(++colIndex);
        CellUtil.setCellStyleProperty(cell, CellUtil.DATA_FORMAT,
            BuiltinFormats.getBuiltinFormat("text"));
        cell.setCellValue(statusText);
      }

      if (dislayedColumnFields.contains("accountNumber")) {
        cell = dataRow.createCell(++colIndex);
        CellUtil.setCellStyleProperty(cell, CellUtil.DATA_FORMAT,
            BuiltinFormats.getBuiltinFormat("text"));
        cell.setCellValue(customer.getAccountNumber());
      }

      if (dislayedColumnFields.contains("gender")) {
        cell = dataRow.createCell(++colIndex);
        CellUtil.setCellStyleProperty(cell, CellUtil.DATA_FORMAT,
            BuiltinFormats.getBuiltinFormat("text"));

        if (customer.getGender() != null) {
          String genderCode = null;
          switch (customer.getGender()) {
            case "M":
              genderCode = "Male";
              break;
            case "F":
              genderCode = "Female";
              break;
            default:
              break;
          }
          cell.setCellValue(messageSource.getMessage(genderCode)); // NOSONAR
        }
      }
    }
  }
}
