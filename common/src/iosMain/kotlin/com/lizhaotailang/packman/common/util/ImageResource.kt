package com.lizhaotailang.packman.common.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import org.jetbrains.skia.Image
import platform.UIKit.UIImage
import platform.UIKit.UIImagePNGRepresentation
import platform.posix.memcpy

@OptIn(ExperimentalForeignApi::class)
@Composable
internal fun imageResource(id: String): ImageBitmap {
    // TODO: maybe use something more efficient
    // TODO: maybe https://github.com/touchlab/DroidconKotlin/blob/main/shared-ui/src/iosMain/kotlin/co/touchlab/droidcon/ui/util/ToSkiaImage.kt
    val image = UIImage.imageNamed(id)!!
    val data = UIImagePNGRepresentation(image)!!
    val byteArray = ByteArray(data.length.toInt()).apply {
        usePinned {
            memcpy(it.addressOf(0), data.bytes, data.length)
        }
    }
    return Image.makeFromEncoded(byteArray).toComposeImageBitmap()
}
