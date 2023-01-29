package com.statista.code.challenge.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.statista.code.challenge.domainobjects.Currency;
import com.statista.code.challenge.domainobjects.department.Department;
import com.statista.code.challenge.domainobjects.department.EuropeDepartment;
import com.statista.code.challenge.domainobjects.department.PaymentProposal;
import com.statista.code.challenge.exceptions.NotValidEmailException;
import com.statista.code.challenge.exceptions.ServerNotReachableException;
import com.statista.code.challenge.exceptions.ServerNotSupportedException;
import com.statista.code.challenge.service.BookingResult;
import com.statista.code.challenge.service.BookingService;
import com.statista.code.challenge.service.DepartmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//
//        package de.rola.coolschrank.controller;
//
//        import com.fasterxml.jackson.core.type.TypeReference;
//        import com.fasterxml.jackson.databind.ObjectMapper;
//        import de.rola.coolschrank.service.ItemParameter;
//        import de.rola.coolschrank.service.ItemService;
//        import de.rola.coolschrank.service.outbound.exception.RemoteServerConflictException;
//        import de.rola.coolschrank.service.outbound.exception.RemoteServerObjectNotFoundException;
//        import org.junit.jupiter.api.Test;
//        import org.springframework.beans.factory.annotation.Autowired;
//        import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//        import org.springframework.boot.test.mock.mockito.MockBean;
//        import org.springframework.test.web.servlet.MockMvc;
//        import org.springframework.test.web.servlet.MvcResult;
//
//        import java.util.List;
//
//        import static com.google.common.truth.Truth.assertThat;
//        import static org.mockito.Mockito.when;
//        import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//        import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//        import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(CoolschrankController.class)
//class CoolschrankControllerTest {
//
//    @MockBean
//    private ItemService itemService;
//    @Autowired
//    private MockMvc mockMvc;
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Test
//    void testGetFavoriteItemsShoppingList_happyCase() throws Exception {
//        String fridgeId = "fridge_id_1234";
//        List<ItemParameter> itemParameters = List.of(
//                new ItemParameter("Joghurt", "1", 5),
//                new ItemParameter("Obst", "2", 10),
//                new ItemParameter("Fleisch", "3", 15));
//        when(itemService.obtainFavoriteItemsShoppingList(fridgeId)).thenReturn(itemParameters);
//        MvcResult mvcResult = mockMvc
//                .perform(get("/rola/coolschrank/favoriteItems/fridge/" + fridgeId))
//                .andExpect(status().isOk())
//                .andReturn();
//        String content = mvcResult.getResponse().getContentAsString();
//        List<ItemParameter> shoppingList = objectMapper.readValue(content, new TypeReference<>() {
//        });
//        assertThat(shoppingList).hasSize(3);
//        assertThat(shoppingList).isEqualTo(itemParameters);
//    }
//
@WebMvcTest(BookingController.class)
class BookingControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BookingService bookingService;
    @MockBean
    DepartmentService departmentService;
    @Autowired
    private ObjectMapper objectMapper;

    private final static String BASIC_URL = "/bookingservice";

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void testCreateBookingAndSendEmail() throws Exception {
        BookingDTO bookingDTO = createBookingDTO("muestermann@gmail.com");
        BookingResult expectedBookingResult = mockServices(bookingDTO);
        MvcResult mvcResult = mockMvc.perform(post(BASIC_URL + "/booking")
                        .content(asJsonString(bookingDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();
        BookingResult actualBookingResult = getResponseObject(mvcResult);
        assertThat(actualBookingResult).isEqualTo(expectedBookingResult);
    }

    @Test
    void testCreateBookingAndSendEmail_WithNotValidEmailException() throws Exception {
        BookingDTO bookingDTO = createBookingDTO("muestermann.gmail.com");
        mockServicesWithException(bookingDTO, NotValidEmailException.class);
        mockMvc.perform(post(BASIC_URL + "/booking")
                        .content(asJsonString(bookingDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isPreconditionFailed());
    }

    @Test
    void testCreateBookingAndSendEmail_ThrowsServerNotReachableException() throws Exception {
        BookingDTO bookingDTO = createBookingDTO("muestermann@server.foobar");
        mockServicesWithException(bookingDTO, ServerNotReachableException.class);
        mockMvc.perform(post(BASIC_URL + "/booking")
                        .content(asJsonString(bookingDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isServiceUnavailable());
    }

    @Test
    void testCreateBookingAndSendEmail_ThrowsServerNotSupportedException() throws Exception {
        BookingDTO bookingDTO = createBookingDTO("muestermann@foobar.org");
        mockServicesWithException(bookingDTO, ServerNotSupportedException.class);
        mockMvc.perform(post(BASIC_URL + "/booking")
                        .content(asJsonString(bookingDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isPreconditionFailed());
    }

    @Test
    void testPersistBooking() throws Exception {
        BookingDTO bookingDTO = createBookingDTO("muestermann@gmail.com");
        UUID bookingId = UUID.randomUUID();
        BookingResult expectedBookingResult = mockServices(bookingDTO, bookingId);
        MvcResult mvcResult = mockMvc.perform(put(BASIC_URL + "/booking/" + bookingId)
                        .content(asJsonString(bookingDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        BookingResult actualBookingResult = getResponseObject(mvcResult);
        assertThat(actualBookingResult).isEqualTo(expectedBookingResult);
    }

    @Test
    void testGetBookingById() throws Exception {
        UUID bookingId = UUID.randomUUID();
        BookingResult expectedBookingResult = mockServices(bookingId);
        MvcResult mvcResult = mockMvc.perform(get(BASIC_URL + "/booking/" + bookingId))
                .andExpect(status().isOk())
                .andReturn();
        BookingResult actualBookingResult = getResponseObject(mvcResult);
        assertThat(actualBookingResult).isEqualTo(expectedBookingResult);
    }

    @Test
    void testGetBookingById_ThrowsNotSuchElementException() throws Exception {
        UUID bookingId = UUID.randomUUID();
        EuropeDepartment department = new EuropeDepartment(UUID.randomUUID(), "EUROPE REPORTS");
        when(departmentService.retrieveDepartment(department.getName())).thenReturn(department);
        when(bookingService.retrieveBooking(bookingId)).thenThrow(NoSuchElementException.class);
        mockMvc.perform(get(BASIC_URL + "/booking/" + bookingId))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetBookingsByDepartments() throws Exception {
        UUID departmentId = UUID.randomUUID();
        List<BookingResult> expectedBookings = List.of(new BookingResult(), new BookingResult());
        when(bookingService.retrieveBookings(departmentId)).thenReturn(expectedBookings);
        MvcResult mvcResult = mockMvc.perform(get(BASIC_URL + "/bookings/department/" + departmentId))
                .andExpect(status().isOk())
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        List<BookingResult> actualBookingResults = objectMapper.readValue(content, new TypeReference<>() {
        });
        assertThat(actualBookingResults).hasSize(2).isEqualTo(expectedBookings);
    }

    @Test
    void testGetBookingsByDepartments_NoDepartmentFound() throws Exception {
        UUID departmentId = UUID.randomUUID();
        when(bookingService.retrieveBookings(departmentId)).thenReturn(Collections.emptyList());
        MvcResult mvcResult = mockMvc.perform(get(BASIC_URL + "/bookings/department/" + departmentId))
                .andExpect(status().isOk())
                .andReturn();
        List<BookingResult> bookingResults = getResponseObject(mvcResult);
        assertThat(bookingResults).isEmpty();
    }

    @Test
    void testGetCurrentUsedCurrencies() throws Exception {
        List<String> expectedCurrencies = List.of(Currency.EURO.name(), Currency.YUAN.name());
        when(bookingService.retrieveCurrentUsedCurrencies()).thenReturn(expectedCurrencies);
        MvcResult mvcResult = mockMvc.perform(get(BASIC_URL + "/bookings/currencies"))
                .andExpect(status().isOk())
                .andReturn();
        List<String> actualCurrencies = getResponseObject(mvcResult);
        assertThat(actualCurrencies).hasSize(2).isEqualTo(expectedCurrencies);
    }

    @Test
    void testGetBookingsTotalPriceByCurrency() throws Exception {
        Currency euro = Currency.EURO;
        BigDecimal expectedTotalPrice = BigDecimal.valueOf(100.00);
        when(bookingService.retrieveBookingsTotalPriceByCurrency(euro)).thenReturn(expectedTotalPrice);
        MvcResult mvcResult = mockMvc.perform(get(BASIC_URL + "/sum/" + euro))
                .andExpect(status().isOk())
                .andReturn();
        BigDecimal actualTotalPrice = getResponseObject(mvcResult);
        assertThat(actualTotalPrice).isEqualTo(expectedTotalPrice);
    }

    @Test
    void testGetBookingPaymentProposal() throws Exception {
        UUID bookingId = UUID.randomUUID();
        PaymentProposal expectedPaymentProposal = new PaymentProposal(BigDecimal.TEN, BigDecimal.ZERO, BigDecimal.ONE);
        when(bookingService.determineBookingPaymentProposal(bookingId)).thenReturn(expectedPaymentProposal);
        MvcResult mvcResult = mockMvc.perform(get(BASIC_URL + "/booking/paymentProposal/" + bookingId))
                .andExpect(status().isOk())
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        PaymentProposal actualPaymentProposal = objectMapper.readValue(content, new TypeReference<>() {
        });
        assertThat(actualPaymentProposal).isEqualTo(expectedPaymentProposal);
    }

    private BookingResult mockServices(BookingDTO bookingDTO) {
        EuropeDepartment department = new EuropeDepartment(UUID.randomUUID(), bookingDTO.departmentName());
        when(departmentService.retrieveDepartment(department.getName())).thenReturn(department);
        BookingResult bookingResult = parseBooking(bookingDTO, department);
        when(bookingService.createBookingAndSendEmail(bookingDTO, department)).thenReturn(bookingResult);
        return bookingResult;
    }

    private BookingResult mockServices(BookingDTO bookingDTO, UUID bookingId) {
        EuropeDepartment department = new EuropeDepartment(UUID.randomUUID(), bookingDTO.departmentName());
        when(departmentService.retrieveDepartment(department.getName())).thenReturn(department);
        BookingResult bookingResult = parseBooking(bookingDTO, department);
        when(bookingService.persistBooking(bookingDTO, bookingId, department)).thenReturn(bookingResult);
        return bookingResult;
    }

    private BookingResult mockServices(UUID bookingId) {
        BookingDTO bookingDTO = createBookingDTO("muestermann@gmail.com");
        EuropeDepartment department = new EuropeDepartment(UUID.randomUUID(), bookingDTO.departmentName());
        when(departmentService.retrieveDepartment(department.getName())).thenReturn(department);
        BookingResult bookingResult = parseBooking(bookingDTO, department);
        when(bookingService.retrieveBooking(bookingId)).thenReturn(bookingResult);
        return bookingResult;
    }

    private <T extends RuntimeException> void mockServicesWithException(BookingDTO bookingDTO, Class<T> exceptionClass) {
        EuropeDepartment department = new EuropeDepartment(UUID.randomUUID(), bookingDTO.departmentName());
        when(departmentService.retrieveDepartment(department.getName())).thenReturn(department);
        when(bookingService.createBookingAndSendEmail(bookingDTO, department)).thenThrow(exceptionClass);
    }

    private <T> T getResponseObject(MvcResult mvcResult) throws UnsupportedEncodingException, JsonProcessingException {
        String content = mvcResult.getResponse().getContentAsString();
        return objectMapper.readValue(content, new TypeReference<>() {
        });
    }

    private String asJsonString(final Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }

    private BookingDTO createBookingDTO(String email) {
        return new BookingDTO("reports", BigDecimal.valueOf(25.00), Currency.USD, LocalDate.now(), email, "EUROPE REPORTS");
    }

    private BookingResult parseBooking(BookingDTO bookingDTO, Department department) {
        return new BookingResult(bookingDTO.description(), bookingDTO.price(), Currency.USD, bookingDTO.subscriptionStartDate(), bookingDTO.email(), department.getName());
    }
}