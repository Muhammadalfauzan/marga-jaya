//package com.example.margajaya.ui.detail
//
//import android.content.Context
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import com.example.margajaya.R
//import com.google.android.material.bottomsheet.BottomSheetDialogFragment
//
//class SuccesOrderBottomSheet : BottomSheetDialogFragment() {
//
//    interface OnActionListener {
//        fun onNavigateToHistory()
//    }
//
//    private var listener: OnActionListener? = null
//
//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        parentFragment?.let {
//            if (it is OnActionListener) {
//                listener = it
//            }
//        }
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val view = inflater.inflate(R.layout.bottom_sheet_success_order, container, false)
//
//        val btnGoToHistory = view.findViewById<Button>(R.id.btnGoToHistory)
//        val btnDismiss = view.findViewById<Button>(R.id.btnDismiss)
//
//        btnGoToHistory.setOnClickListener {
//            listener?.onNavigateToHistory()
//            dismiss()
//        }
//
//        btnDismiss.setOnClickListener {
//            dismiss()
//        }
//
//        return view
//    }
//
//    companion object {
//        const val TAG = "SuccessOrderBottomSheet"
//    }
//}
