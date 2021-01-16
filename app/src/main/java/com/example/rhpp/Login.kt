package com.example.rhpp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.rhpp.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
@ExperimentalCoroutinesApi
class Login : Fragment(R.layout.fragment_login) {
    private var _binding : FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel :  LoginViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         _binding = FragmentLoginBinding.inflate(inflater,container,false)
        binding.viewmodel = viewModel
        val view = binding.root
        return view}
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnLogin.setOnClickListener {
            viewModel.login(binding.etUsername.text.toString(),
                    binding.spinnerJabatan.selectedItem.toString(),
                    binding.etPassword.text.toString())
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.loginUiState.collect{
                when(it){
                    is LoginViewModel.LoginUiState.Success -> {
                        Snackbar.make(view,"Activity LOL",Snackbar.LENGTH_LONG).show()
                        if(binding.spinnerJabatan.selectedItem.toString()=="Plasma"){
                        findNavController().navigate(LoginDirections.actionLoginToPlasma(
                                binding.etUsername.text.toString()))
                         }else{
                             findNavController().navigate(LoginDirections.actionLoginToInternal(
                                     binding.spinnerJabatan.selectedItem.toString()))
                         }}
                    is LoginViewModel.LoginUiState.Error -> {
                        Snackbar.make(
                                view,
                                it.message,
                                Snackbar.LENGTH_LONG
                        ).show()
                    }
                    is LoginViewModel.LoginUiState.Loading -> {
                        Snackbar.make(view, "Please wait...", Snackbar.LENGTH_LONG).show()
                    }else -> Unit

                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}