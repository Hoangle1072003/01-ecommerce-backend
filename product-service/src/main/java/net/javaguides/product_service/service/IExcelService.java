package net.javaguides.product_service.service;

import net.javaguides.product_service.shema.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IExcelService {
    void processFile(MultipartFile file);
}
