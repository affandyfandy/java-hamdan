package com.midterm.group4.controller;

import com.midterm.group4.data.model.Invoice;
import com.midterm.group4.dto.InvoiceMapper;
import com.midterm.group4.dto.OrderItemMapper;
import com.midterm.group4.dto.request.CreateInvoiceDTO;
import com.midterm.group4.dto.response.ReadInvoiceDTO;
import com.midterm.group4.exception.ObjectNotFoundException;
import com.midterm.group4.service.ExcelExportService;
import com.midterm.group4.service.InvoiceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.HttpHeaders;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = InvoiceController.class)
 class InvoiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InvoiceService invoiceService;

    @MockBean
    private InvoiceMapper invoiceMapper;

    @MockBean
    private OrderItemMapper orderItemMapper;

    @MockBean
    private ExcelExportService excelExportService;

    private UUID invoiceId;
    private Invoice invoice;
    private ReadInvoiceDTO readInvoiceDTO;

    @BeforeEach
    public void setup() {
        invoiceId = UUID.randomUUID();
        invoice = new Invoice();
        invoice.setInvoiceId(invoiceId);

        readInvoiceDTO = new ReadInvoiceDTO();
        readInvoiceDTO.setInvoiceId(invoiceId);

        new CreateInvoiceDTO();
    }

    @Test
     void testGetAllInvoice() throws Exception {
        when(invoiceService.findAllSorted(anyInt(), anyInt(), anyString(), anyString()))
                .thenReturn(new PageImpl<>(Collections.singletonList(invoice)));

        when(invoiceMapper.toReadInvoiceDto(any(Invoice.class))).thenReturn(readInvoiceDTO);

        mockMvc.perform(get("/api/v1/invoice")
                        .param("pageNo", "0")
                        .param("pageSize", "10")
                        .param("sortOrder", "asc")
                        .param("sortBy", "totalAmount")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].invoiceId").value(invoiceId.toString()));

        verify(invoiceService, times(1)).findAllSorted(anyInt(), anyInt(), anyString(), anyString());
        verify(invoiceMapper, times(1)).toReadInvoiceDto(any(Invoice.class));
    }

    @Test
     void testGetInvoiceById() throws Exception {
        when(invoiceService.findById(any(UUID.class))).thenReturn(invoice);
        when(invoiceMapper.toReadInvoiceDto(any(Invoice.class))).thenReturn(readInvoiceDTO);

        mockMvc.perform(get("/api/v1/invoice/{id}", invoiceId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.invoiceId").value(invoiceId.toString()));

        verify(invoiceService, times(1)).findById(invoiceId);
        verify(invoiceMapper, times(1)).toReadInvoiceDto(invoice);
    }

    @Test
     void testGetInvoiceById_NotFound() throws Exception {
        when(invoiceService.findById(any(UUID.class))).thenThrow(new ObjectNotFoundException("Invoice not found"));

        mockMvc.perform(get("/api/v1/invoice/{id}", invoiceId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Invoice not found"));

        verify(invoiceService, times(1)).findById(invoiceId);
    }

    @Test
     void testAddNewInvoice() throws Exception {
        when(invoiceMapper.toEntity(any(CreateInvoiceDTO.class))).thenReturn(invoice);
        when(invoiceService.createInvoice(any(Invoice.class), anyList())).thenReturn(invoice);
        when(invoiceMapper.toReadInvoiceDto(any(Invoice.class))).thenReturn(readInvoiceDTO);

        mockMvc.perform(post("/api/v1/invoice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"customerId\": \"" + UUID.randomUUID() + "\", \"listOrderItem\": []}"))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.invoiceId").value(invoiceId.toString()));

        verify(invoiceService, times(1)).createInvoice(any(Invoice.class), anyList());
        verify(invoiceMapper, times(1)).toReadInvoiceDto(invoice);
    }

    @Test
     void testUpdateInvoice() throws Exception {
        when(invoiceMapper.toEntity(any(CreateInvoiceDTO.class))).thenReturn(invoice);
        when(invoiceService.update(any(UUID.class), any(Invoice.class), anyList())).thenReturn(invoice);
        when(invoiceMapper.toReadInvoiceDto(any(Invoice.class))).thenReturn(readInvoiceDTO);

        mockMvc.perform(put("/api/v1/invoice/{id}", invoiceId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"customerId\": \"" + UUID.randomUUID() + "\", \"listOrderItem\": []}"))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.invoiceId").value(invoiceId.toString()));

        verify(invoiceService, times(1)).update(any(UUID.class), any(Invoice.class), anyList());
        verify(invoiceMapper, times(1)).toReadInvoiceDto(invoice);
    }

    @Test
     void testSearchInvoiceByCustomerName() throws Exception {
        when(invoiceService.findAllByCustomerName(anyInt(), anyInt(), anyString(), anyString(), anyString()))
                .thenReturn(new PageImpl<>(Collections.singletonList(invoice)));

        when(invoiceMapper.toReadInvoiceDto(any(Invoice.class))).thenReturn(readInvoiceDTO);

        mockMvc.perform(get("/api/v1/invoice/search")
                        .param("pageNo", "0")
                        .param("pageSize", "10")
                        .param("sortOrder", "asc")
                        .param("sortBy", "totalAmount")
                        .param("customerName", "John Doe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].invoiceId").value(invoiceId.toString()));

        verify(invoiceService, times(1)).findAllByCustomerName(anyInt(), anyInt(), anyString(), anyString(), anyString());
        verify(invoiceMapper, times(1)).toReadInvoiceDto(any(Invoice.class));
    }

    @Test
     void testGetReport() throws Exception {
        BigInteger totalAmountPerDay = new BigInteger("1000");
        when(invoiceService.getTotalAmountPerDay(any(LocalDate.class))).thenReturn(totalAmountPerDay);
        when(invoiceService.getTop3ProductsByAmount()).thenReturn(Collections.emptyList());
        when(invoiceService.getSoldProducts()).thenReturn(Collections.emptyList());
        when(invoiceService.getTotalQuantityPerProduct()).thenReturn(Collections.emptyMap());
        when(invoiceService.getTotalAmountPerProduct()).thenReturn(Collections.emptyMap());

        mockMvc.perform(get("/api/v1/invoice/report")
                        .param("date", "2024-08-10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['Revenue generated this day']").value(totalAmountPerDay.toString()));

        verify(invoiceService, times(1)).getTotalAmountPerDay(any(LocalDate.class));
    }

    @Test
     void testExportInvoicesToExcel() throws Exception {

        when(invoiceService.getInvoicesByFilter(any(UUID.class), anyInt(), anyInt()))
                .thenReturn(Collections.singletonList(invoice));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        when(excelExportService.exportInvoice(anyList())).thenReturn(outputStream);


        mockMvc.perform(get("/api/v1/invoice/export")
                        .param("customerId", UUID.randomUUID().toString())
                        .param("month", "8")
                        .param("year", "2024"))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoices.xlsx"))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .andExpect(header().string(HttpHeaders.CONTENT_LENGTH, String.valueOf(outputStream.size())));


        verify(invoiceService, times(1)).getInvoicesByFilter(any(UUID.class), anyInt(), anyInt());
        verify(excelExportService, times(1)).exportInvoice(anyList());
    }
}

