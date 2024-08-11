package com.midterm.group4.controller;

import com.midterm.group4.data.model.Product;
import com.midterm.group4.dto.ProductMapper;
import com.midterm.group4.dto.request.CreateProductDTO;
import com.midterm.group4.dto.response.ReadProductDTO;
import com.midterm.group4.utils.ExcelTestUtils;
import com.midterm.group4.exception.ObjectNotFoundException;
import com.midterm.group4.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
 class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private ProductMapper productMapper;

    @MockBean
    private ExcelTestUtils excelTestUtils;

    private UUID productId;
    private Product product;
    private ReadProductDTO readProductDTO;

    @BeforeEach
    public void setup() {
        productId = UUID.randomUUID();
        product = new Product();
        product.setProductId(productId);

        readProductDTO = new ReadProductDTO();
        readProductDTO.setProductId(productId);

        new CreateProductDTO();
    }

    @Test
     void testGetAllProduct() throws Exception {
        when(productService.findAllSorted(anyInt(), anyInt(), anyString(), anyString()))
                .thenReturn(new PageImpl<>(Collections.singletonList(product), PageRequest.of(0, 10), 1));

        when(productMapper.toReadProductDto(any(Product.class))).thenReturn(readProductDTO);

        mockMvc.perform(get("/api/v1/product")
                        .param("pageNo", "0")
                        .param("pageSize", "10")
                        .param("sortOrder", "asc")
                        .param("sortBy", "name"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].productId").value(productId.toString()));

        verify(productService, times(1)).findAllSorted(anyInt(), anyInt(), anyString(), anyString());
        verify(productMapper, times(1)).toReadProductDto(any(Product.class));
    }

    @Test
     void testGetProductById() throws Exception {
        when(productService.findById(any(UUID.class))).thenReturn(product);
        when(productMapper.toReadProductDto(any(Product.class))).thenReturn(readProductDTO);

        mockMvc.perform(get("/api/v1/product/{id}", productId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(productId.toString()));

        verify(productService, times(1)).findById(productId);
        verify(productMapper, times(1)).toReadProductDto(product);
    }

    @Test
     void testGetProductById_NotFound() throws Exception {
        when(productService.findById(any(UUID.class))).thenThrow(new ObjectNotFoundException("Product not found"));

        mockMvc.perform(get("/api/v1/product/{id}", productId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Product not found"));

        verify(productService, times(1)).findById(productId);
    }

    @Test
     void testCreateProduct() throws Exception {
        when(productMapper.toEntity(any(CreateProductDTO.class))).thenReturn(product);
        when(productService.save(any(Product.class))).thenReturn(product);
        when(productMapper.toReadProductDto(any(Product.class))).thenReturn(readProductDTO);

        mockMvc.perform(post("/api/v1/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"New Product\"}"))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.productId").value(productId.toString()));

        verify(productService, times(1)).save(any(Product.class));
        verify(productMapper, times(1)).toReadProductDto(product);
    }

    @Test
     void testUpdateProduct() throws Exception {
        when(productMapper.toEntity(any(CreateProductDTO.class))).thenReturn(product);
        when(productService.update(any(UUID.class), any(Product.class))).thenReturn(product);
        when(productMapper.toReadProductDto(any(Product.class))).thenReturn(readProductDTO);

        mockMvc.perform(put("/api/v1/product/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated Product\"}"))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.productId").value(productId.toString()));

        verify(productService, times(1)).update(any(UUID.class), any(Product.class));
        verify(productMapper, times(1)).toReadProductDto(product);
    }

    @Test
     void testActivateProduct() throws Exception {
        when(productService.findById(any(UUID.class))).thenReturn(product);
        when(productMapper.toReadProductDto(any(Product.class))).thenReturn(readProductDTO);

        mockMvc.perform(post("/api/v1/product/{id}/activate", productId))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.productId").value(productId.toString()));

        verify(productService, times(1)).updateStatus(productId, true);
        verify(productService, times(1)).findById(productId);
        verify(productMapper, times(1)).toReadProductDto(product);
    }

    @Test
     void testDeactivateProduct() throws Exception {
        when(productService.findById(any(UUID.class))).thenReturn(product);
        when(productMapper.toReadProductDto(any(Product.class))).thenReturn(readProductDTO);

        mockMvc.perform(post("/api/v1/product/{id}/deactivate", productId))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.productId").value(productId.toString()));

        verify(productService, times(1)).updateStatus(productId, false);
        verify(productService, times(1)).findById(productId);
        verify(productMapper, times(1)).toReadProductDto(product);
    }

    @Test
     void testSearchProduct() throws Exception {
        when(productService.findAllByQuery(anyInt(), anyInt(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(new PageImpl<>(Collections.singletonList(product)));

        when(productMapper.toReadProductDto(any(Product.class))).thenReturn(readProductDTO);

        mockMvc.perform(get("/api/v1/product/search")
                        .param("pageNo", "0")
                        .param("pageSize", "10")
                        .param("sortOrder", "asc")
                        .param("sortBy", "name")
                        .param("name", "Product")
                        .param("status", "active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].productId").value(productId.toString()));

        verify(productService, times(1)).findAllByQuery(anyInt(), anyInt(), anyString(), anyString(), anyString(), anyString());
        verify(productMapper, times(1)).toReadProductDto(any(Product.class));
    }
}
