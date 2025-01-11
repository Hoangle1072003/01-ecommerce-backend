package net.javaguides.identity_service.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * File: null.java
 * Author: Le Van Hoang
 * Date: 1/6/2025 (06/01/2025)
 * Time: 10:02 PM
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

public interface IExcelService {
    void processFile(MultipartFile file);
}
