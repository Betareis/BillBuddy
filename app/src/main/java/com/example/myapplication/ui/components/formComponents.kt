import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun EditTransactionForm() {
    var transaction_name by rememberSaveable { mutableStateOf("") }
    var transaction_price by rememberSaveable { mutableStateOf("") }
    Column {
        TextField(
            value = transaction_name,
            onValueChange = { transaction_name = it },
            label = { Text("Enter new transaction name") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )
        Spacer(modifier = Modifier.height(10.dp))
        TextField(
            value = transaction_price,
            onValueChange = { transaction_price = it },
            label = { Text("Enter new transaction price") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )

        TextField(
            value = transaction_name,
            onValueChange = { transaction_name = it },
            label = { Text("Enter new transaction name") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )

        TextField(
            value = transaction_name,
            onValueChange = { transaction_name = it },
            label = { Text("Enter new transaction name") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )

    }
}