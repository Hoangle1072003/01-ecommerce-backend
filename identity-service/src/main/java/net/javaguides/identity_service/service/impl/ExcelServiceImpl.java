package net.javaguides.identity_service.service.impl;

import lombok.RequiredArgsConstructor;
import net.javaguides.identity_service.domain.User;
import net.javaguides.identity_service.repository.IUserRepository;
import net.javaguides.identity_service.service.IExcelService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * File: ExcelServiceImpl.java
 * Author: Le Van Hoang
 * Date: 1/6/2025 (06/01/2025)
 * Time: 10:02 PM
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Service
@RequiredArgsConstructor
public class ExcelServiceImpl implements IExcelService {
    private final IUserRepository userRepository;

    @Override
    public void processFile(MultipartFile file) {
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            List<User> users = new ArrayList<>();
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;
                User user = new User();
                user.setName(row.getCell(0).getStringCellValue());
                user.setEmail(row.getCell(1).getStringCellValue());
                user.setPassword(row.getCell(2).getNumericCellValue() + "");
                users.add(user);
            }
            userRepository.saveAll(users);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
