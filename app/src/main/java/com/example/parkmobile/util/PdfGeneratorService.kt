package com.example.parkmobile.util

import android.content.Context
import android.os.Environment
import androidx.core.content.ContextCompat
import com.example.parkmobile.R
import com.example.parkmobile.data.model.HistoricoEstacionamento
import com.itextpdf.kernel.colors.DeviceRgb
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.borders.SolidBorder
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.properties.TextAlignment
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

object PdfGeneratorService {

    fun gerarRecibosPdf(context: Context, recibos: List<HistoricoEstacionamento>): File {
        val pdfPath = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        val file = File(pdfPath, "RelatorioDeRecibos_${System.currentTimeMillis()}.pdf")

        val writer = PdfWriter(file)
        val pdfDocument = PdfDocument(writer)
        val document = Document(pdfDocument)

        // CORES
        val primaryInt = ContextCompat.getColor(context, R.color.md_theme_primary)
        val secondaryInt = ContextCompat.getColor(context, R.color.md_theme_secondary)
        val primaryColor = DeviceRgb((primaryInt shr 16) and 0xFF, (primaryInt shr 8) and 0xFF, primaryInt and 0xFF)
        val secondaryColor = DeviceRgb((secondaryInt shr 16) and 0xFF, (secondaryInt shr 8) and 0xFF, secondaryInt and 0xFF)

        // TÍTULO APLICATIVO
        document.add(
            Paragraph("ParkMobile") // nome do app
                .setBold()
                .setFontSize(18f)
                .setFontColor(primaryColor)
                .setTextAlignment(TextAlignment.CENTER)
        )

        // LINHA DE SEPARAÇÃO 1
        document.add(
            Paragraph("")
                .setBorderBottom(SolidBorder(primaryColor, 1f))
                .setMarginTop(4f)
                .setMarginBottom(8f)
        )

        // TÍTULO “RELATÓRIO DE RECIBOS”
        document.add(
            Paragraph("Relatório de Recibos")
                .setBold()
                .setFontSize(16f)
                .setFontColor(primaryColor)
                .setTextAlignment(TextAlignment.CENTER)
        )

        // LINHA DE SEPARAÇÃO 2
        document.add(
            Paragraph("")
                .setBorderBottom(SolidBorder(primaryColor, 1f))
                .setMarginTop(4f)
                .setMarginBottom(12f)
        )

        // SUBTÍTULO "RELATÓRIO"
        document.add(
            Paragraph("Relatório")
                .setBold()
                .setFontSize(14f)
                .setFontColor(secondaryColor)
                .setTextAlignment(TextAlignment.LEFT)
                .setMarginBottom(8f)
        )

        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

        // BLOCO POR RECIBO + LINHA ENTRE ELES
        recibos.forEachIndexed { index, item ->
            val text = StringBuilder().apply {
                append("Recibo: ${item.recibo}\n")
                append("Placa: ${item.placaVeiculo}\n")
                append("Veículo: ${item.marcaVeiculo} ${item.modeloVeiculo}\n")
                append("Check-in: ${item.checkIn?.let { d -> sdf.format(d) } ?: "N/A"}\n")
                append("Check-out: ${item.checkOut?.let { d -> sdf.format(d) } ?: "N/A"}\n")
                append(String.format(Locale.getDefault(), "Valor: R$ %.2f", item.valor ?: 0.0))
            }.toString()

            document.add(
                Paragraph(text)
                    .setMarginLeft(8f)
                    .setMarginRight(8f)
            )

            if (index < recibos.lastIndex) {
                document.add(
                    Paragraph("")
                        .setBorderBottom(SolidBorder(secondaryColor, 0.8f))
                        .setMarginTop(6f)
                        .setMarginBottom(6f)
                )
            }
        }

        document.close()
        return file
    }
}
