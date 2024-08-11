package com.midterm.group4.utils;

import com.midterm.group4.data.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class FileUtilsTest {

    @Test
    void testReadEmployeeFromExcel_Success() throws IOException {

        MultipartFile file = ExcelTestUtils.createDummyExcelFile();


        List<Product> products = FileUtils.readEmployeeFromExcel(file);


        assertNotNull(products);
        assertEquals(2, products.size());

        Product product1 = products.get(0);
        assertEquals("Product A", product1.getName());
        assertEquals(100, product1.getPrice().intValue());
        assertEquals(50, product1.getQuantity());
        assertTrue(product1.isActive());

        Product product2 = products.get(1);
        assertEquals("Product B", product2.getName());
        assertEquals(150, product2.getPrice().intValue());
        assertEquals(30, product2.getQuantity());
        assertFalse(product2.isActive());
    }
}
