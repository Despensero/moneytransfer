package task.moneytransfer.service

import io.swagger.annotations.ApiOperation
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import task.moneytransfer.domain.Account
import task.moneytransfer.repository.AccountRepository
import javax.persistence.EntityNotFoundException

@Service
@Transactional(isolation = Isolation.SERIALIZABLE)
class TransferService(private val accountRepository: AccountRepository) {

    fun findAccounts(): List<Account> = accountRepository.findAll()

    fun putMoney(accountNumber: String, amount: Long) {
        if (amount < 0) {
            throw IllegalStateException("Negative amount of money")
        }
        val account = accountRepository.findById(accountNumber).orElseThrow { EntityNotFoundException("No account found with number $accountNumber") }
        account.value += amount
    }

    fun getMoney(accountNumber: String, amount: Long) {
        if (amount < 0) {
            throw IllegalStateException("Negative amount of money")
        }
        val account = accountRepository.findById(accountNumber).orElseThrow { EntityNotFoundException("No account found with number $accountNumber") }
        if (account.value >= amount) {
            account.value -= amount
        } else {
            throw IllegalStateException("Not enough money on account $accountNumber")
        }
    }

    fun transferMoney(accountNumberFrom: String, accountNumberTo: String, amount: Long) {
        getMoney(accountNumberFrom, amount)
        putMoney(accountNumberTo, amount)
    }
}