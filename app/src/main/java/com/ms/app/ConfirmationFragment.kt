package com.ms.app

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ms.app.databinding.ConfirmationFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
@AndroidEntryPoint
class ConfirmationFragment : Fragment() {

    @Inject lateinit var activity: AppCompatActivity

    private lateinit var userViewModel: UserViewModel

    private var _binding: ConfirmationFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userViewModel = ViewModelProvider(activity).get(UserViewModel::class.java)

        activity.onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                userViewModel.logoutUser()
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = ConfirmationFragmentBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel.userLiveData.observe(viewLifecycleOwner, Observer { user ->
            user?.let {
                binding.emailDisplay.text = user.emailAddress
                binding.nameDisplay.setTextOrGone(user.firstName)
                binding.websiteDisplay.setTextOrGone(user.website)

                user.firstName?.let {
                    binding.greetingText.text = getString(R.string.hello_with_name, it)
                } ?: run {
                    binding.greetingText.text = getString(R.string.hello)
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}