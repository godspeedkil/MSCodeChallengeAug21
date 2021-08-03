package com.ms.app

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ms.app.databinding.ProfileCreationFragmentBinding
import com.ms.persistence.User
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Shown to the user when starting, provides input fields for profile creation
 */
@AndroidEntryPoint
class ProfileCreationFragment : Fragment() {

    @Inject lateinit var activity: AppCompatActivity

    lateinit var userViewModel: UserViewModel

    private var _binding: ProfileCreationFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = ProfileCreationFragmentBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel = ViewModelProvider(activity).get(UserViewModel::class.java)

        binding.submitButton.setOnClickListener {
            submitNewUser()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun submitNewUser() {
        viewLifecycleOwner.lifecycleScope.launch {
            val newUser = attemptCreateNewUser()

            if (newUser == null) {
                Toast.makeText(context, R.string.invalid_empty_fields, Toast.LENGTH_LONG).show()
                return@launch
            }

            if (userViewModel.addNewUser(newUser)) {
                findNavController().navigate(R.id.action_profile_creation_to_confirmation)
            } else {
                Toast.makeText(context, R.string.email_already_in_db, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun attemptCreateNewUser() : User? {
        if (binding.emailEdit.text.isNullOrBlank() || binding.passwordEdit.text.isNullOrBlank()) {
            return null
        }

        return User(
            emailAddress = binding.emailEdit.text.toString(),
            firstName = binding.firstNameEdit.text.toString().nullIfBlank(),
            password = binding.passwordEdit.text.toString(),
            website = binding.websiteEdit.text.toString().nullIfBlank()
        )
    }

}