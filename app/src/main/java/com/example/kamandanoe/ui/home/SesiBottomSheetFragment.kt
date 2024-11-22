package com.example.kamandanoe.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.kamandanoe.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SesiBottomSheetFragment : BottomSheetDialogFragment() {

    interface OnSessionSelectedListener {
        fun onSessionSelected(session: String)
    }

    private var sessionSelectedListener: OnSessionSelectedListener? = null
    private lateinit var sessionAdapter: ArrayAdapter<String>
    private val sessions = listOf(
        "Semua Sesi", "09:00 - 10:00", "10:00 - 11:00",
        "13:00 - 14:00", "14:00 - 15:00", "15:00 - 16:00"
    )

    private val viewModel: HomeViewModel by activityViewModels()
    private var hasSelectedSession: Boolean = false
    private var lastSelectedSession: String? = null // Track the last selected session

    override fun onAttach(context: Context) {
        super.onAttach(context)
        sessionSelectedListener = parentFragment as? OnSessionSelectedListener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_sesi, container, false)
        val listView: ListView = view.findViewById(R.id.sessionListView)
        val btnApply: Button = view.findViewById(R.id.btnApply)
        val btnCancel: Button = view.findViewById(R.id.btnResetAll)

        sessionAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_single_choice, sessions)
        listView.adapter = sessionAdapter
        listView.choiceMode = ListView.CHOICE_MODE_SINGLE

        // Observe the selected session from ViewModel and set initial selection
        viewModel.selectedSession.observe(viewLifecycleOwner) { selected ->
            val index = sessions.indexOf(selected).takeIf { it >= 0 } ?: 0
            listView.setItemChecked(index, true)
            hasSelectedSession = selected != "Semua Sesi"
            lastSelectedSession = selected
        }

        var selectedSession: String? = viewModel.selectedSession.value

        listView.setOnItemClickListener { _, _, position, _ ->
            selectedSession = sessions[position]
            hasSelectedSession = true
        }

        btnApply.setOnClickListener {
            selectedSession?.let {
                sessionSelectedListener?.onSessionSelected(it)
                viewModel.updateSelectedSession(it)
                dismiss()
            } ?: run {
                Toast.makeText(requireContext(), "Pilih sesi terlebih dahulu", Toast.LENGTH_SHORT).show()
            }
        }

        btnCancel.setOnClickListener {
            if (hasSelectedSession) {
                // Reset to "Semua Sesi" if cancel is clicked
                selectedSession = "Semua Sesi"
                listView.setItemChecked(0, true)
                viewModel.updateSelectedSession("Semua Sesi")
                sessionSelectedListener?.onSessionSelected("Semua Sesi") // Notify the parent fragment
                dismiss() // Dismiss the bottom sheet
            } else {
                dismiss() // Simply dismiss if no selection change
            }
        }

        return view
    }
}
