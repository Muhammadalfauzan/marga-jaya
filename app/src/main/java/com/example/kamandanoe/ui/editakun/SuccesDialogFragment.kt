package com.example.kamandanoe.ui.editakun

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class SuccesDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle("Berhasil")
            .setMessage("Profil berhasil diperbarui.")
            .setPositiveButton("OK") { _, _ ->
                targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, null)
                dismiss()
            }
            .setCancelable(false)
            .create()
    }

}


