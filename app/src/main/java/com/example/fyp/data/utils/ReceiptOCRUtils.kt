package com.example.fyp.utils

import android.content.Context
import android.net.Uri
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.tasks.await
import timber.log.Timber

object ReceiptOCRUtils {

    suspend fun processImage(context: Context, imageUri: Uri): String? {
        return try {
            val image = InputImage.fromFilePath(context, imageUri)
            val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
            val result = recognizer.process(image).await()
            result.text
        } catch (e: Exception) {
            Timber.e(e, "Failed to process image for OCR")
            null
        }
    }

    fun extractFields(text: String): Map<String, Any> {
        val fields = mutableMapOf<String, Any>()

        val cleanedText = text.replace("â‚¬", "€")
        val lines = cleanedText.lines().map { it.trim() }.filter { it.isNotBlank() }

        val dateRegex = Regex("""[A-Z][a-z]+ \d{1,2}, \d{4}""")
        val priceRegex = Regex("""€\s?(\d+[,.]\d{2})""")

        val date = dateRegex.find(cleanedText)?.value ?: "Not found"
        fields["date"] = date

        val priceLines = lines.filter { priceRegex.containsMatchIn(it) }
        val prices = priceLines.mapNotNull {
            priceRegex.find(it)?.groupValues?.get(1)?.replace(",", ".")?.toFloatOrNull()
        }
        val amount = prices.maxOrNull() ?: 0f
        val itemPrices = prices.filter { it < amount }
        fields["amount"] = amount

        val merchantEndIndex = lines.indexOfFirst {
            it.contains("receipt", true) || it.contains("item", true) || dateRegex.containsMatchIn(it)
        }.takeIf { it > 0 } ?: lines.size

        val merchant = lines.subList(0, merchantEndIndex)
            .joinToString(" ")
            .replace(Regex("[^A-Za-z0-9\\s'&-]"), "")
            .split(" ")
            .joinToString(" ") { it.lowercase().replaceFirstChar(Char::uppercaseChar) }
        fields["merchant"] = merchant

        val itemStart = lines.indexOfFirst { it.equals("Item", true) }
        val totalLine = lines.indexOfFirst { it.contains("total", true) }

        val itemNames = if (itemStart in 0 until totalLine) {
            lines.subList(itemStart + 1, totalLine)
                .filter {
                    val lower = it.lowercase()
                    lower !in listOf("receipt", "total") && !priceRegex.containsMatchIn(it) && !dateRegex.containsMatchIn(it)
                }
        } else emptyList()

        val items = itemNames.zip(itemPrices).map { (name, price) ->
            "$name €%.2f".format(price)
        }

        fields["items"] = items
        return fields
    }
}