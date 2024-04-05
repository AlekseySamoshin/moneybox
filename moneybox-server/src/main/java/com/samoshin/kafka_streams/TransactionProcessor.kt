package com.samoshin.kafka_streams

import com.samoshin.dto.MoneyTransferDto
import com.samoshin.service.MoneyTransferService
import lombok.RequiredArgsConstructor
import org.apache.kafka.streams.kstream.KStream
import org.apache.kafka.streams.kstream.Materialized
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.function.Consumer

@Configuration
@RequiredArgsConstructor
open class TransactionProcessor(
    private val moneyTransferService: MoneyTransferService? = null
) {
    private val countStore = "count-store"
    @Bean
    open fun makeTransactionProcessor(): Consumer<KStream<String?, MoneyTransferDto>> {
        return Consumer { input: KStream<String?, MoneyTransferDto> ->
            val countTable = input.groupByKey()
                .count(Materialized.`as`(countStore))
            input.leftJoin(
                countTable
            ) { value: MoneyTransferDto, count: Long? ->
                MoneyTransferDto(
                    value.id,
                    value.moneyboxId,
                    count,
                    value.isIncrease,
                    value.sum
                )
            }.peek { key: String?, transfer: MoneyTransferDto? -> moneyTransferService!!.makeTransaction(transfer) }
        }
    }

    @get:Bean
    open val infoProcessor: Consumer<KStream<String?, String?>>
        get() = Consumer { input: KStream<String?, String?> ->
            input.peek { key: String?, value: String? ->
                moneyTransferService!!.getInfo(
                    value
                )
            }
        }
}