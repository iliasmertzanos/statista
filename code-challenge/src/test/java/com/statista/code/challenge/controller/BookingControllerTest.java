package com.statista.code.challenge.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.statista.code.challenge.domainobjects.Currency;
import com.statista.code.challenge.domainobjects.department.Department;
import com.statista.code.challenge.domainobjects.department.EuropeDepartment;
import com.statista.code.challenge.service.BookingResult;
import com.statista.code.challenge.service.BookingService;
import com.statista.code.challenge.service.DepartmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
public class BookingControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BookingService bookingService;
    @MockBean
    DepartmentService departmentService;

    private final static String BASIC_URL = "/bookingservice";

    @Test
    void test_createBookingAndSendEmail() throws Exception {
        BookingDTO bookingDTO = createBookingDTO("muestermann@gmail.com");
        EuropeDepartment department = new EuropeDepartment(UUID.randomUUID(), "");
        when(bookingService.createBookingAndSendEmail(bookingDTO, department)).thenReturn(parseBooking(bookingDTO, department));
        when(departmentService.retrieveDepartment(department.getName())).thenReturn(department);
        MvcResult mvcResult = mockMvc.perform(post(BASIC_URL + "/booking")
                        .content(asJsonString(bookingDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
    }

    public static String asJsonString(final Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.writeValueAsString(obj);
    }

    private BookingDTO createBookingDTO(String email) {
        return new BookingDTO("reports", BigDecimal.valueOf(25.00), Currency.USD, LocalDate.now(), email, "EUROPE REPORTS");
    }

    private BookingResult parseBooking(BookingDTO bookingDTO, Department department) {
        return new BookingResult(bookingDTO.description(), bookingDTO.price(), Currency.USD, bookingDTO.subscriptionStartDate(), bookingDTO.email(), department.getName());
    }
}
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
//    @Test
//    void testGetFavoriteItemsShoppingList_RemoteServerObjectNotFoundException() throws Exception {
//        String fridgeId = "fridge_id_1234";
//        when(itemService.obtainFavoriteItemsShoppingList(fridgeId)).thenThrow(RemoteServerObjectNotFoundException.class);
//        mockMvc
//                .perform(get("/rola/coolschrank/favoriteItems/fridge/" + fridgeId))
//                .andExpect(status().is4xxClientError())
//                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(RemoteServerObjectNotFoundException.class));
//    }
//
//    @Test
//    void testGetFavoriteItemsShoppingList_RemoteServerConflictException() throws Exception {
//        String fridgeId = "fridge_id_1234";
//        when(itemService.obtainFavoriteItemsShoppingList(fridgeId)).thenThrow(RemoteServerConflictException.class);
//        mockMvc
//                .perform(get("/rola/coolschrank/favoriteItems/fridge/" + fridgeId))
//                .andExpect(status().is4xxClientError())
//                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(RemoteServerConflictException.class));
//    }
//
//    @Test
//    void testGetMissingItemsShoppingList_happyCase() throws Exception {
//        String fridgeId = "fridge_id_1234";
//        List<ItemParameter> itemParameters = List.of(
//                new ItemParameter("Joghurt", "1", 5),
//                new ItemParameter("Obst", "2", 10),
//                new ItemParameter("Fleisch", "3", 15));
//        when(itemService.obtainMissingItemsShoppingList(fridgeId)).thenReturn(itemParameters);
//        MvcResult mvcResult = mockMvc
//                .perform(get("/rola/coolschrank/missingItems/fridge/" + fridgeId))
//                .andExpect(status().isOk())
//                .andReturn();
//        String content = mvcResult.getResponse().getContentAsString();
//        List<ItemParameter> shoppingList = objectMapper.readValue(content, new TypeReference<>() {
//        });
//        assertThat(shoppingList).hasSize(3);
//        assertThat(shoppingList).isEqualTo(itemParameters);
//    }
//
//    @Test
//    void testGetMissingItemsShoppingList_RemoteServerObjectNotFoundException() throws Exception {
//        String fridgeId = "fridge_id_1234";
//        when(itemService.obtainMissingItemsShoppingList(fridgeId)).thenThrow(RemoteServerObjectNotFoundException.class);
//        mockMvc
//                .perform(get("/rola/coolschrank/missingItems/fridge/" + fridgeId))
//                .andExpect(status().is4xxClientError())
//                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(RemoteServerObjectNotFoundException.class));
//    }
//
//    @Test
//    void testGetMissingItemsShoppingList_RemoteServerConflictException() throws Exception {
//        String fridgeId = "fridge_id_1234";
//        when(itemService.obtainMissingItemsShoppingList(fridgeId)).thenThrow(RemoteServerConflictException.class);
//        mockMvc
//                .perform(get("/rola/coolschrank/missingItems/fridge/" + fridgeId))
//                .andExpect(status().is4xxClientError())
//                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(RemoteServerConflictException.class));
//    }
//
//    @Test
//    void testChangeItem_happyCase() throws Exception {
//        String fridgeId = "fridge_id_1234";
//        String itemId = "1234";
//        ItemParameter expected = new ItemParameter("Joghurt", "1", 5);
//        when(itemService.changeItem(fridgeId, itemId, 5)).thenReturn(expected);
//        MvcResult mvcResult = mockMvc
//                .perform(post("/rola/coolschrank/fridge/" + fridgeId + "/item/" + itemId + "/newAmount/" + 5))
//                .andExpect(status().isOk())
//                .andReturn();
//        String content = mvcResult.getResponse().getContentAsString();
//        ItemParameter itemParameter = objectMapper.readValue(content, ItemParameter.class);
//        assertThat(itemParameter).isEqualTo(expected);
//    }
//
//    @Test
//    void testChangeItem_RemoteServerObjectNotFoundException() throws Exception {
//        String fridgeId = "fridge_id_1234";
//        String itemId = "1234";
//        when(itemService.changeItem(fridgeId, itemId, 5)).thenThrow(RemoteServerObjectNotFoundException.class);
//        mockMvc
//                .perform(post("/rola/coolschrank/fridge/" + fridgeId + "/item/" + itemId + "/newAmount/" + 5))
//                .andExpect(status().is4xxClientError())
//                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(RemoteServerObjectNotFoundException.class));
//    }
//
//    @Test
//    void testChangeItem_RemoteServerConflictException() throws Exception {
//        String fridgeId = "fridge_id_1234";
//        String itemId = "1234";
//        when(itemService.changeItem(fridgeId, itemId, 5)).thenThrow(RemoteServerObjectNotFoundException.class);
//        mockMvc
//                .perform(post("/rola/coolschrank/fridge/" + fridgeId + "/item/" + itemId + "/newAmount/" + 5))
//                .andExpect(status().is4xxClientError())
//                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(RemoteServerObjectNotFoundException.class));
//    }
//}