package com.ms.app

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.ms.app.databinding.ConfirmationFragmentBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class ConfirmationFragment : Fragment() {

    private var _binding: ConfirmationFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = ConfirmationFragmentBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signInButton.setOnClickListener {
            findNavController().navigate(R.id.action_confirmation_to_profile_creation)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}