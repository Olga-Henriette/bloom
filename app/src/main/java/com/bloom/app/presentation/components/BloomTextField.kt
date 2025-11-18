package com.bloom.app.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bloom.app.presentation.theme.*

@Composable
fun BloomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    isPassword: Boolean = false,
    enabled: Boolean = true,
    isError: Boolean = false,
    errorMessage: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    onImeAction: () -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        if (label.isNotEmpty()) {
            Text(
                text = label,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = if (enabled) BloomColors.neutral900 else BloomColors.neutral600,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(49.dp),
            enabled = enabled,
            singleLine = true,
            placeholder = {
                if (placeholder.isNotEmpty()) {
                    Text(
                        text = placeholder,
                        color = BloomColors.neutral600,
                        fontSize = 16.sp
                    )
                }
            },
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            keyboardActions = KeyboardActions(
                onAny = { onImeAction() }
            ),
            textStyle = LocalTextStyle.current.copy(
                fontSize = 16.sp,
                lineHeight = 26.sp
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = BloomColors.neutral600,
                unfocusedTextColor = BloomColors.neutral600,
                focusedBorderColor = BloomColors.neutral300,
                unfocusedBorderColor = BloomColors.neutral300,
                focusedContainerColor = BloomColors.white,
                unfocusedContainerColor = BloomColors.white,
                cursorColor = BloomColors.primary500,
                disabledTextColor = BloomColors.neutral600,
                disabledBorderColor = BloomColors.neutral300,
                disabledContainerColor = BloomColors.white,
                errorBorderColor = BloomColors.danger500,
                errorTextColor = BloomColors.neutral900,
                errorContainerColor = BloomColors.white,
                errorCursorColor = BloomColors.danger500
            ),
            shape = RoundedCornerShape(10.dp),
            isError = isError
        )

        if (isError && errorMessage != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = errorMessage,
                color = BloomColors.danger500,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 12.dp)
            )
        }
    }
}

@Composable
fun BloomPasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Password",
    enabled: Boolean = true,
    isError: Boolean = false,
    errorMessage: String? = null,
    imeAction: ImeAction = ImeAction.Done,
    onImeAction: () -> Unit = {}
) {
    BloomTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        modifier = modifier,
        isPassword = true,
        enabled = enabled,
        isError = isError,
        errorMessage = errorMessage,
        keyboardType = KeyboardType.Password,
        imeAction = imeAction,
        onImeAction = onImeAction
    )
}

@Composable
fun BloomEmailField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Email Address",
    enabled: Boolean = true,
    isError: Boolean = false,
    errorMessage: String? = null,
    imeAction: ImeAction = ImeAction.Next,
    onImeAction: () -> Unit = {}
) {
    BloomTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        modifier = modifier,
        enabled = enabled,
        isError = isError,
        errorMessage = errorMessage,
        keyboardType = KeyboardType.Email,
        imeAction = imeAction,
        onImeAction = onImeAction
    )
}