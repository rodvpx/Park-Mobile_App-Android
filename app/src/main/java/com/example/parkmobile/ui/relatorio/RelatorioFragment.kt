package com.example.parkmobile.ui.relatorio

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.parkmobile.R
import com.example.parkmobile.data.model.HistoricoEstacionamento
import com.example.parkmobile.ui.historico.HistoricoViewModel
import com.example.parkmobile.ui.historico.HistoricoViewModelFactory
import com.example.parkmobile.util.PdfGeneratorService
import java.io.File

class RelatorioFragment : Fragment() {

    private val viewModel: HistoricoViewModel by viewModels { HistoricoViewModelFactory() }
    private var pdfGerado = false // Flag para controlar a geração do PDF

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_relatorios, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnGerarRelatorio: Button = view.findViewById(R.id.btn_gerarRelatorio)

        // Observa a lista de histórico (recibos)
        viewModel.historico.observe(viewLifecycleOwner) { recibos ->
            if (pdfGerado) {
                if (recibos.isNotEmpty()) {
                    val pdfFile = PdfGeneratorService.gerarRecibosPdf(requireContext(), recibos)
                    abrirPdf(pdfFile)
                } else {
                    Toast.makeText(requireContext(), "Não há recibos para gerar o relatório.", Toast.LENGTH_SHORT).show()
                }
                pdfGerado = false // Reseta a flag
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            if (error.isNotBlank()) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()
            }
        }

        btnGerarRelatorio.setOnClickListener {
            Toast.makeText(requireContext(), "Gerando relatório...", Toast.LENGTH_SHORT).show()
            pdfGerado = true // Ativa a flag antes de carregar os dados
            viewModel.carregarHistoricoDoUsuarioLogado()
        }
    }

    private fun abrirPdf(file: File) {
        val authority = "${requireContext().packageName}.provider"
        val uri = FileProvider.getUriForFile(requireContext(), authority, file)

        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/pdf")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(requireContext(), "Nenhum aplicativo encontrado para abrir PDF.", Toast.LENGTH_LONG).show()
        }
    }
}
