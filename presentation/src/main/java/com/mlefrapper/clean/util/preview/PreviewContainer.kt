package com.mlefrapper.clean.util.preview

import androidx.compose.runtime.Composable
import com.mlefrapper.clean.ui.theme.AppTheme

@Composable
fun PreviewContainer(
    content: @Composable () -> Unit
) {
    AppTheme {
        content()
    }
}