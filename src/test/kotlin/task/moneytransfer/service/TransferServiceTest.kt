package task.moneytransfer.service

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import task.moneytransfer.domain.Account
import task.moneytransfer.repository.AccountRepository
import java.util.*
import javax.persistence.EntityNotFoundException

@ExtendWith(MockKExtension::class)
class TransferServiceTest {

    @MockK
    private lateinit var accountRepository: AccountRepository

    @InjectMockKs
    private lateinit var transferService: TransferService


    @Test
    fun `test putMoney success`() {
        val accountNumber = "account number string"
        val value = 200L
        val amount = 300L
        val account = Account(accountNumber, value)
        every { accountRepository.findById(accountNumber) } returns Optional.of(account)
        transferService.putMoney(accountNumber, amount)

        assertTrue(account.value == value + amount) {
            "account should have value ${value + amount} but it's ${account.value}"
        }
    }

    @Test
    fun `test putMoney failure cause of no account found`() {
        val accountNumber = "account number string"
        val amount = 300L
        every { accountRepository.findById(accountNumber) } returns Optional.empty()
        assertThrows(EntityNotFoundException::class.java, { transferService.putMoney(accountNumber, amount) },
                "Service should throw EntityNotFoundException")
    }

    @Test
    fun `test putMoney failure cause of no negative amount`() {
        val accountNumber = "account number string"
        val amount = -300L
        every { accountRepository.findById(accountNumber) } returns Optional.empty()
        assertThrows(IllegalStateException::class.java, { transferService.putMoney(accountNumber, amount) },
                "Service should throw IllegalStateException")
    }

    @Test
    fun `test getMoney success`() {
        val accountNumber = "account number string"
        val value = 300L
        val amount = 200L
        val account = Account(accountNumber, value)
        every { accountRepository.findById(accountNumber) } returns Optional.of(account)
        transferService.getMoney(accountNumber, amount)

        assertTrue(account.value == value - amount) {
            "account should have value ${value - amount} but it's ${account.value}"
        }
    }

    @Test
    fun `test getMoney failure cause of overdraft`() {
        val accountNumber = "account number string"
        val value = 200L
        val amount = 300L
        val account = Account(accountNumber, value)
        every { accountRepository.findById(accountNumber) } returns Optional.of(account)
        assertThrows(IllegalStateException::class.java, { transferService.getMoney(accountNumber, amount) },
                "Service should throw IllegalStateException")
    }

    @Test
    fun `test getMoney failure cause of no account found`() {
        val accountNumber = "account number string"
        val amount = 300L
        every { accountRepository.findById(accountNumber) } returns Optional.empty()
        assertThrows(EntityNotFoundException::class.java, { transferService.getMoney(accountNumber, amount) },
                "Service should throw EntityNotFoundException")
    }

    @Test
    fun `test getMoney failure cause of no negative amount`() {
        val accountNumber = "account number string"
        val amount = -300L
        every { accountRepository.findById(accountNumber) } returns Optional.empty()
        assertThrows(IllegalStateException::class.java, { transferService.getMoney(accountNumber, amount) },
                "Service should throw IllegalStateException")
    }

    @Test
    fun `test transferMoney success`() {
        val accountFromNumber = "account from string"
        val accountFromValue = 200L
        val accountToNumber = "account to string"
        val accountToValue = 50L
        val amount = 100L

        val accountFrom = Account(accountFromNumber, accountFromValue)
        val accountTo = Account(accountToNumber, accountToValue)

        every { accountRepository.findById(accountFromNumber) } returns Optional.of(accountFrom)
        every { accountRepository.findById(accountToNumber) } returns Optional.of(accountTo)

        transferService.transferMoney(accountFromNumber, accountToNumber, amount)

        assertTrue(accountFrom.value == accountFromValue - amount) {
            "accountFrom value should be ${accountFromValue - amount} but it's ${accountFrom.value}"
        }
        assertTrue(accountTo.value == accountToValue + amount) {
            "accountFrom value should be ${accountToValue + amount} but it's ${accountTo.value}"
        }
    }

    @Test
    fun `test transferMoney failure cause of no accountFrom`() {
        val accountFromNumber = "account from string"
        val accountToNumber = "account to string"
        val accountToValue = 50L
        val amount = 100L

        val accountTo = Account(accountToNumber, accountToValue)

        every { accountRepository.findById(accountFromNumber) } returns Optional.empty()
        every { accountRepository.findById(accountToNumber) } returns Optional.of(accountTo)


        assertThrows(EntityNotFoundException::class.java,
                { transferService.transferMoney(accountFromNumber, accountToNumber, amount) },
                "Service should throw EntityNotFoundException")
    }

    @Test
    fun `test transferMoney failure cause of no accountTo`() {
        val accountFromNumber = "account from string"
        val accountFromValue = 200L
        val accountToNumber = "account to string"
        val amount = 100L

        val accountFrom = Account(accountFromNumber, accountFromValue)

        every { accountRepository.findById(accountFromNumber) } returns Optional.of(accountFrom)
        every { accountRepository.findById(accountToNumber) } returns Optional.empty()

        assertThrows(EntityNotFoundException::class.java,
                { transferService.transferMoney(accountFromNumber, accountToNumber, amount) },
                "Service should throw EntityNotFoundException")
    }

    @Test
    fun `test transferMoney failure couse of negative amount`(){
        val accountFromNumber = "account from string"
        val accountToNumber = "account to string"
        val amount = -100L

        assertThrows(IllegalStateException::class.java,
                { transferService.transferMoney(accountFromNumber, accountToNumber, amount) },
                "Service should throw IllegalStateException")
    }

    @Test
    fun `test transferMoney failure cause of overdraft`() {
        val accountFromNumber = "account from string"
        val accountFromValue = 200L
        val accountToNumber = "account to string"
        val accountToValue = 50L
        val amount = 300L

        val accountFrom = Account(accountFromNumber, accountFromValue)
        val accountTo = Account(accountToNumber, accountToValue)

        every { accountRepository.findById(accountFromNumber) } returns Optional.of(accountFrom)
        every { accountRepository.findById(accountToNumber) } returns Optional.of(accountTo)

        assertThrows(IllegalStateException::class.java,
                { transferService.transferMoney(accountFromNumber, accountToNumber, amount) },
                "Service should throw IllegalStateException")
    }
}