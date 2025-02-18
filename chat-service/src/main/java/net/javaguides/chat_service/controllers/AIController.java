package net.javaguides.chat_service.controllers;

import lombok.RequiredArgsConstructor;
import net.javaguides.chat_service.dtos.request.ReqAiDto;
import net.javaguides.chat_service.dtos.response.Product;
import net.javaguides.chat_service.dtos.response.ResProductDetailsDto;
import net.javaguides.chat_service.services.AIService;
import net.javaguides.chat_service.services.httpClient.ProductServiceClient;
import net.javaguides.chat_service.utils.constant.TypeChatEnum;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * File: ChatController.java
 * Author: Le Van Hoang
 * Date: 08/02/2025
 * Time: 20:03
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@RestController()
@RequestMapping("/api/qna")
@RequiredArgsConstructor
public class AIController {
    private final AIService aiService;
    private final ProductServiceClient productService;

    //    @PostMapping("/ask")
//    public ResponseEntity<String> askQuestion(@RequestBody Map<String, String> payload) {
//        String question = payload.get("question");
//        String answer = aiService.askQuestion(question);
//
//        return ResponseEntity.ok(answer);
//    }
    @PostMapping("/ask")
    public ResponseEntity<String> askQuestion(@RequestBody ReqAiDto reqAiDto) {
        if (reqAiDto.getType().equals(TypeChatEnum.PRODUCT)) {
            ResProductDetailsDto productInfo = productService.getProduct(reqAiDto.getProductId());

            String prompt = """
                        Dưới đây là thông tin chi tiết về sản phẩm:
                        %s
                        Người dùng hỏi: "%s"
                        Dựa vào thông tin trên, hãy chỉ trả lời đúng phần liên quan đến câu hỏi của người dùng. Không đưa thông tin dư thừa.
                    """.formatted(productInfo, reqAiDto.getQuestion());

            String aiResponse = aiService.askQuestion(prompt);

            return ResponseEntity.ok(aiResponse);
        } else {
            return ResponseEntity.ok(aiService.askQuestion(reqAiDto.getQuestion()));
        }
    }


    private String buildProductDescription(ResProductDetailsDto product) {
        StringBuilder description = new StringBuilder();

        description.append("Tên sản phẩm: ").append(product.getName()).append("\n")
                .append("Thương hiệu: ").append(product.getBrand()).append("\n")
                .append("Mô tả: ").append(product.getDescription()).append("\n")
                .append("Mô tả ngắn: ").append(product.getShortDescription()).append("\n")
                .append("Ngày ra mắt: ").append(product.getReleaseDate()).append("\n")
                .append("Trọng lượng: ").append(product.getWeightInGrams()).append("g\n");

        if (product.getSpecs() != null && !product.getSpecs().isEmpty()) {
            description.append("Thông số kỹ thuật:\n");
            for (Product.Spec spec : product.getSpecs()) {
                description.append("- ").append(spec.getK()).append(": ").append(spec.getV());
                if (spec.getU() != null) {
                    description.append(" ").append(spec.getU());
                }
                description.append("\n");
            }
        }

        if (product.getVarients() != null && !product.getVarients().isEmpty()) {
            description.append("Các phiên bản sản phẩm:\n");
            for (Product.Product_varient varient : product.getVarients()) {
                description.append("- ").append(varient.getName()).append(": ")
                        .append(varient.getPrice()).append(" USD, ")
                        .append(varient.isAvailable() ? "Còn hàng" : "Hết hàng").append("\n");
            }
        }

        return description.toString();
    }

}
