package com.example.parkmobile.util

import android.content.Context
import android.os.Environment
import com.example.parkmobile.data.model.HistoricoEstacionamento
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.properties.TextAlignment
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

object PdfGeneratorService {

    fun gerarRecibosPdf(context: Context, recibos: List<HistoricoEstacionamento>): File {
        // Define o nome e o caminho do arquivo
        val pdfPath = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        val file = File(pdfPath, "RelatorioDeRecibos_${System.currentTimeMillis()}.pdf")

        // Inicializa o escritor de PDF
        val writer = PdfWriter(file)
        val pdfDocument = PdfDocument(writer)
        val document = Document(pdfDocument)

        // Adiciona o Título
        document.add(Paragraph("Relatório de Recibos").setBold().setFontSize(20f).setTextAlignment(TextAlignment.CENTER))
        document.add(Paragraph("\n")) // Linha em branco

        // Adiciona cada recibo ao documento
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        recibos.forEach {
            document.add(Paragraph("Recibo: ${it.recibo}").setBold())
            document.add(Paragraph("Placa: ${it.placaVeiculo}"))
            document.add(Paragraph("Veículo: ${it.marcaVeiculo} ${it.modeloVeiculo}"))
            document.add(Paragraph("Check-in: ${it.checkIn?.let { date -> sdf.format(date) } ?: "N/A"}"))
            document.add(Paragraph("Check-out: ${it.checkOut?.let { date -> sdf.format(date) } ?: "N/A"}"))
            document.add(Paragraph(String.format(Locale.getDefault(), "Valor: R$ %.2f", it.valor ?: 0.0)))
            document.add(Paragraph("\n--------------------\n").setTextAlignment(TextAlignment.CENTER))
        }

        // Fecha o documento
        document.close()

        return file
    }
}
