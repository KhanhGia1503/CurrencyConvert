package com.example.currencychange

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    lateinit var sourceAmount: EditText
    lateinit var sourceCurrency: Spinner
    lateinit var targetAmount: EditText
    lateinit var targetCurrency: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Liên kết các thành phần giao diện
        sourceAmount = findViewById(R.id.sourceAmount)
        sourceCurrency = findViewById(R.id.sourceCurrency)
        targetAmount = findViewById(R.id.targetAmount)
        targetCurrency = findViewById(R.id.targetCurrency)

        // Thiết lập sự kiện lắng nghe khi nhập dữ liệu
        sourceAmount.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                updateConversion()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Thiết lập sự kiện lắng nghe khi chọn đồng tiền nguồn
        sourceCurrency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                updateConversion()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // Thiết lập sự kiện lắng nghe khi chọn đồng tiền đích
        targetCurrency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                updateConversion()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    // Hàm cập nhật khi người dùng thay đổi số tiền hoặc chọn loại tiền
    private fun updateConversion() {
        // Lấy giá trị từ EditText và Spinner
        val amount = sourceAmount.text.toString().toDoubleOrNull() ?: return
        val sourceCurrencyType = sourceCurrency.selectedItem.toString()
        val targetCurrencyType = targetCurrency.selectedItem.toString()

        // Chuyển đổi tiền tệ
        val convertedAmount = convertCurrency(amount, sourceCurrencyType, targetCurrencyType)

        // Định dạng kết quả
        val formattedAmount = String.format("%,.2f", convertedAmount) // Giữ lại 2 chữ số sau dấu thập phân và thêm dấu phẩy

        // Cập nhật giá trị sau khi chuyển đổi
        targetAmount.setText(formattedAmount)
    }

    // Hàm chuyển đổi tiền tệ với tỷ giá giả định
    private fun convertCurrency(amount: Double, source: String, target: String): Double {
        // Tỷ giá giả định giữa các loại tiền tệ
        val exchangeRates = mapOf(
            "USD" to 1.0,            // 1 USD
            "EUR" to 0.94,           // 1 EUR = 0.94 USD
            "VND" to 23800.0,        // 1 USD = 23,800 VND
            "JPY" to 149.25,         // 1 USD = 149.25 JPY
            "GBP" to 1.25            // 1 GBP = 1.25 USD
        )

        val sourceRate = exchangeRates[source] ?: return 0.0 // Trả về 0 nếu loại tiền tệ không hợp lệ
        val targetRate = exchangeRates[target] ?: return 0.0 // Trả về 0 nếu loại tiền tệ không hợp lệ

        // Chuyển đổi
        return (amount / sourceRate) * targetRate
    }
}
