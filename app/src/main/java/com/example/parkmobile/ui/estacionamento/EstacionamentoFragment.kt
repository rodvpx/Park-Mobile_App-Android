package com.example.parkmobile.ui.estacionamento

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.parkmobile.R

class EstacionamentoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_estacionamento, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Card Check-in
        val cardCheckIn = view.findViewById<View>(R.id.card_check_in)
        val checkInImage = cardCheckIn.findViewById<ImageView>(R.id.card_image)
        val checkInText = cardCheckIn.findViewById<TextView>(R.id.card_text)
        val checkInButton = cardCheckIn.findViewById<Button>(R.id.btn_gerarRelatorio) // Reusing the button id

        checkInImage.setImageResource(R.drawable.bg_check_in)
        checkInText.text = "Registre a entrada de um novo veículo no estacionamento."
        checkInButton.text = "Check-in"
        checkInButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, CheckInFragment())
                .addToBackStack(null)
                .commit()
        }

        // Card Check-out
        val cardCheckOut = view.findViewById<View>(R.id.card_check_out)
        val checkOutImage = cardCheckOut.findViewById<ImageView>(R.id.card_image)
        val checkOutText = cardCheckOut.findViewById<TextView>(R.id.card_text)
        val checkOutButton = cardCheckOut.findViewById<Button>(R.id.btn_gerarRelatorio) // Reusing the button id

        checkOutImage.setImageResource(R.drawable.bg_check_out)
        checkOutText.text = "Registre a saída de um veículo e finalize a cobrança."
        checkOutButton.text = "Check-out"
        checkOutButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, CheckOutFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}
