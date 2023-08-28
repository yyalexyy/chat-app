package com.example.whatsapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.whatsapp.domain.model.User
import com.example.whatsapp.domain.use_case.AuthenticationUseCase
import com.example.whatsapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val authUseCase : AuthenticationUseCase
) : ViewModel() {

    lateinit var iViews: IViewsHandling

    fun signInWithPhoneNumber(phoneNumber: String, activity: MainActivity) {
        iViews = activity
        viewModelScope.launch {
            authUseCase.phoneNumberSignIn(phoneNumber, activity).collect {
                when(it) {
                    is Resource.Loading -> {
                        iViews.showProgressBar()
                    }

                    is Resource.Error -> {
                        iViews.showError(it.message?:"An Error Occurred in AuthenticationViewModel::signInWithPhoneNumber()")
                    }

                    is Resource.Success -> {
                        iViews.hideProgressBar()
                        iViews.dismissOtpBottomSheetDialogFragment()
                        iViews.changeViewsVisibility()
                    }
                }
            }
        }
    }

    fun createUserProfile(user: User) {
        viewModelScope.launch {
            authUseCase.createUserProfile(user = user, authUseCase.getUserId()).collectLatest {
                when(it) {
                    is Resource.Loading -> {
                        iViews.showProgressBar()
                    }

                    is Resource.Error -> {
                        iViews.showError(it.message?:"An Error Occurred in AuthenticationViewModel::createUserProfile()")
                    }

                    is Resource.Success -> {
                        iViews.hideProgressBar()
                        iViews.showHomePage()
                    }
                }
            }
        }
    }

    fun isUserAuthenticated() = authUseCase.isUserAuthenticated()
}