package task.moneytransfer.domain


import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class Account(
        @Id val num: String,
        var value: Long
)