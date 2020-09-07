package task.moneytransfer.controller

import io.swagger.annotations.ApiParam
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import task.moneytransfer.domain.Account
import task.moneytransfer.service.TransferService

@RestController
@RequestMapping("/money")
class TransferController(private val transferService: TransferService) {

    @GetMapping
    fun getAccounts(): String {
        return transferService.findAccounts().toString()
    }

    @PostMapping("/put")
    fun putMoney(accountNumber: String, amount: Long): Unit =
            transferService.putMoney(accountNumber, amount)

    @PostMapping("/get")
    fun getMoney(accountNumber: String, amount: Long) =
            transferService.getMoney(accountNumber, amount)

    @PostMapping("/transfer")
    fun transferMoney(accountFrom: String,
                      accountTo: String,
                      amount: Long) =
            transferService.transferMoney(accountFrom, accountTo, amount)
}