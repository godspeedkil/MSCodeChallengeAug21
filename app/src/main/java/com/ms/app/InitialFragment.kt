package com.ms.app

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ms.app.databinding.InitialFragmentBinding
import com.ms.persistence.User
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Shown to the user when starting, provides input fields for profile creation
 */
@AndroidEntryPoint
class InitialFragment : Fragment() {

    @Inject lateinit var activity: AppCompatActivity

    private lateinit var userViewModel: UserViewModel

    private var isCreateProfile: Boolean = true

    private var _binding: InitialFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = InitialFragmentBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel = ViewModelProvider(activity).get(UserViewModel::class.java)

        binding.submitButton.setOnClickListener {
            createNewUser()
        }

        binding.signInButton.setOnClickListener {
            signIn()
        }

        binding.passwordEdit.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT && !isCreateProfile) {
                signIn()
            } else {
                binding.websiteEdit.requestFocus()
            }

            true
        }

        binding.websiteEdit.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO && isCreateProfile) {
                createNewUser()
            }

            true
        }

        binding.loginCta.setOnClickListener {
            isCreateProfile = !isCreateProfile
            switchExperience(isCreateProfile)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun clearEditTexts() {
        binding.emailEdit.clearFocus()
        binding.emailEdit.text?.clear()
        binding.firstNameEdit.clearFocus()
        binding.firstNameEdit.text?.clear()
        binding.passwordEdit.clearFocus()
        binding.passwordEdit.text?.clear()
        binding.websiteEdit.clearFocus()
        binding.websiteEdit.text?.clear()
    }

    private fun hideSoftKeyboard() {
        context?.let {
            val imm = getSystemService(it, InputMethodManager::class.java) as InputMethodManager
            imm.hideSoftInputFromWindow(view?.windowToken, 0)
        }
    }

    /**
     * Swap between profile creation/sign in experience
     */
    private fun switchExperience(isCreateProfile: Boolean) {
        hideSoftKeyboard()
        clearEditTexts()

        binding.submitButton.show(isCreateProfile)
        binding.signInButton.show(!isCreateProfile)

        binding.firstNameEdit.show(isCreateProfile)
        binding.websiteEdit.show(isCreateProfile)

        binding.subheaderText.text =
            if (isCreateProfile) {
                getString(R.string.profile_creation_description)
            } else {
                getString(R.string.sign_in_description)
            }

        binding.headerText.text =
            if (isCreateProfile) {
                getString(R.string.profile_creation)
            } else {
                getString(R.string.sign_in)
            }

        binding.loginCta.text =
            if (isCreateProfile) {
                getString(R.string.sign_in_instead)
            } else {
                getString(R.string.return_to_profile_creation)
            }
    }

    private fun signIn() {
        viewLifecycleOwner.lifecycleScope.launch {
            val emailField = binding.emailEdit.text
            val passwordField = binding.passwordEdit.text

            val retrievedUser =
                if (emailField.isNullOrBlank() || passwordField.isNullOrBlank()) {
                    null
                } else {
                    userViewModel.loginUser(emailField.toString(), passwordField.toString())
                }

            retrievedUser?.let {
                findNavController().navigate(R.id.action_profile_creation_to_confirmation)
            } ?: run {
                Toast.makeText(context, R.string.invalid_email_and_password_combination, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun createNewUser() {
        viewLifecycleOwner.lifecycleScope.launch {
            val newUser = User.attemptInstantiateUser(
                binding.emailEdit.text,
                binding.firstNameEdit.text,
                binding.passwordEdit.text,
                binding.websiteEdit.text
            )

            if (newUser == null) {
                Toast.makeText(context, R.string.invalid_empty_fields, Toast.LENGTH_LONG).show()
                return@launch
            }

            if (userViewModel.addNewUserToDatabase(newUser)) {
                findNavController().navigate(R.id.action_profile_creation_to_confirmation)
            } else {
                Toast.makeText(context, R.string.email_already_in_db, Toast.LENGTH_LONG).show()
            }
        }
    }

}