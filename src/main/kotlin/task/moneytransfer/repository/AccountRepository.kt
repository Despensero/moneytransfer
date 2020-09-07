package task.moneytransfer.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import task.moneytransfer.domain.Account

@Repository
interface AccountRepository : JpaRepository<Account, String>