package com.rnmoyasar

import android.os.Parcelable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moyasar.android.sdk.PaymentConfig
import com.moyasar.android.sdk.PaymentResult
import com.moyasar.android.sdk.PaymentSheet
import com.moyasar.android.sdk.payment.models.Payment
import kotlinx.parcelize.Parcelize

class CheckoutViewModel : ViewModel() {
    val status = MutableLiveData<Status>(Status.Idle)
    val payment = MutableLiveData<Payment?>(null)
    var paymentSheet: PaymentSheet? = null
    val config = PaymentConfig(
            amount = 10000,
            currency = "SAR",
            description = "Sample Android SDK Payment",
            apiKey = "pk_test_xC83McLo9dh9pJdtbDcor3jQNyyyn1NtJ8k4Naq5",
            baseUrl = "https://api.moyasar.com",
            manual = false,
            metadata = mapOf(
                    "order_id" to "order_123"
            ),
            createSaveOnlyToken = false,
    )

    fun registerForActivity(activity: CheckoutActivity) {
        paymentSheet = PaymentSheet(activity, { onPaymentSheetResult(it) }, config)
    }

    fun beginDonation() {
        if (paymentSheet == null) {
            status.value = Status.Failed(Exception("Payment sheet was not setup for view model"))
            return
        }

        paymentSheet!!.present()
    }

    fun onPaymentSheetResult(result: PaymentResult) {
        when (result) {
            PaymentResult.Canceled -> status.value = Status.Failed(Exception("User canceled"))
            is PaymentResult.Failed -> status.value = Status.Failed(Exception("Payment failed"))
            is PaymentResult.Completed -> {
                payment.value = result.payment
                when (result.payment.status) {
                    "paid", "authorized" -> status.value = Status.Success
                    else -> status.value = Status.Success
                }
            }
            is PaymentResult.CompletedToken -> {
                print("Got newly created token ${result.token.id}")
            }
        }
    }

    sealed class Status : Parcelable {
        @Parcelize
        object Idle : Status()

        @Parcelize
        object Success : Status()

        @Parcelize
        data class Failed(val e: Exception) : Status()
    }
}