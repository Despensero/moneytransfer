package task.moneytransfer.controller

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import task.moneytransfer.service.TransferService

@WebMvcTest(TransferController::class)
class TransferControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var transferService: TransferService


    @Test
    fun putMoney() {
        Mockito.`when`(transferService.putMoney(Mockito.anyString(), Mockito.anyLong())).then { }
        mockMvc.perform(MockMvcRequestBuilders.post("/money/put")
                .queryParam("accountNumber", "2128506")
                .queryParam("amount", "300")
        ).andExpect { result -> assertTrue(result.response.status == HttpStatus.OK.value()) {"Wrong result status"} }
    }

    @Test
    fun getMoney() {
        Mockito.`when`(transferService.getMoney(Mockito.anyString(), Mockito.anyLong())).then { }
        mockMvc.perform(MockMvcRequestBuilders.post("/money/get")
                .queryParam("accountNumber", "2128506")
                .queryParam("amount", "300")
        ).andExpect { result -> assertTrue(result.response.status == HttpStatus.OK.value()) {"Wrong result status"} }
    }

    @Test
    fun transferMoney() {
        Mockito.`when`(transferService.transferMoney(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong())).then { }
        mockMvc.perform(MockMvcRequestBuilders.post("/money/transfer")
                .queryParam("accountFrom", "2128506")
                .queryParam("accountTo", "212850A")
                .queryParam("amount", "300")
        ).andExpect { result -> assertTrue(result.response.status == HttpStatus.OK.value()) {"Wrong result status"} }
    }
}