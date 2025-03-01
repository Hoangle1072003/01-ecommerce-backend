package net.javaguides.product_service.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import net.javaguides.product_service.repository.IProductRepository;
import net.javaguides.product_service.service.IExcelService;
import net.javaguides.product_service.shema.Product;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExcelServiceImpl implements IExcelService {
    private final IProductRepository productRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void processFile(MultipartFile file) {
        List<Product> products = new ArrayList<>();

        try (InputStream is = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;

                Product product = new Product();
                getCellValueAsString(row, 0).ifPresent(product::setId);
                getCellValueAsString(row, 1).ifPresent(product::setCode);
                getCellValueAsString(row, 2).ifPresent(product::setName);
                getCellValueAsString(row, 3).ifPresent(product::setBrand);
                getCellValueAsString(row, 4).ifPresent(product::setDescription);
                getCellValueAsString(row, 5).ifPresent(product::setShortDescription);
                if (row.getCell(6) != null && row.getCell(6).getCellType() == CellType.NUMERIC) {
                    product.setReleaseDate(row.getCell(6).getLocalDateTimeCellValue().toInstant(ZoneOffset.UTC));
                }
                getCellValueAsInt(row, 7).ifPresent(product::setWeightInGrams);
                getCellValueAsString(row, 8).ifPresent(product::setCategoryID);
                // SpecsJSON
                getCellValueAsString(row, 9).ifPresent(specsJson -> product.setSpecs(parseSpecs(specsJson)));
                // VariantsJSON
                getCellValueAsString(row, 10).ifPresent(variantsJson -> product.setVarients(parseVariants(variantsJson)));
                if (isProductValid(product)) {
                    products.add(product);
                } else {
                    System.out.println("Product không hợp lệ: " + product);
                }
            }

            // Save products vào MongoDB
            if (!products.isEmpty()) {
                productRepository.saveAll(products);
                System.out.println("Đã lưu " + products.size() + " sản phẩm.");
            } else {
                System.out.println("Không có sản phẩm hợp lệ để lưu.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isProductValid(Product product) {
        return product.getId() != null &&
                product.getCode() != null &&
                product.getName() != null &&
                product.getBrand() != null;
    }


    private Optional<String> getCellValueAsString(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex);
        if (cell != null) {
            if (cell.getCellType() == CellType.STRING) {
                return Optional.of(cell.getStringCellValue().trim());
            } else if (cell.getCellType() == CellType.NUMERIC) {
                return Optional.of(String.valueOf(cell.getNumericCellValue()));
            }
        }
        return Optional.empty();
    }

    private Optional<Double> getCellValueAsDouble(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex);
        if (cell != null && cell.getCellType() == CellType.NUMERIC) {
            return Optional.of(cell.getNumericCellValue());
        }
        return Optional.empty();
    }

    private Optional<Integer> getCellValueAsInt(Row row, int cellIndex) {
        return getCellValueAsDouble(row, cellIndex).map(Double::intValue);
    }

    private List<Product.Spec> parseSpecs(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<>() {});
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    private List<Product.Product_varient> parseVariants(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<>() {});
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}