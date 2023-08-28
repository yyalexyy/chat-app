package com.example.whatsapp.presentation

interface IViewsHandling {

    fun showProgressBar() {}

    fun hideProgressBar() {}

    fun showError(error: String) {}

    fun dismissOtpBottomSheetDialogFragment() {}

    fun changeViewsVisibility() {}

    fun showHomePage() {}
}