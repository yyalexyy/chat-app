package com.example.whatsapp.presentation

interface IViews {

    fun showProgressBar() {}

    fun hideProgressBar() {}

    fun showError(error: String) {}

    fun dismissOtpBottomSheetDialogFragment() {}

    fun changeViewsVisibility() {}

    fun openHomePageLayout() {}
}