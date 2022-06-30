package com.example.userdatabase.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.userdatabase.BaseApplication
import com.example.userdatabase.R
import com.example.userdatabase.databinding.FragmentUserDetailBinding
import com.example.userdatabase.model.User
import com.example.userdatabase.viewmodel.UserViewModel
import com.example.userdatabase.viewmodel.UserViewModelFactory

class UserDetailFragment : Fragment() {

    private val navigationArgs: UserDetailFragmentArgs by navArgs()

    private val viewModel: UserViewModel by activityViewModels {
        UserViewModelFactory(
            (activity?.application as BaseApplication).database.userDao()
        )
    }

    private lateinit var user: User

    private var _binding: FragmentUserDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = navigationArgs.id
        viewModel.getUser(id).observe(this.viewLifecycleOwner) {
            user = it
            bindUser()
        }
    }

    private fun bindUser() {
        binding.apply {
            firstName.text = user.firstName
            lastName.text = user.lastName
            id.text = user.id.toString()
            dateOfBirth.text = user.dateOfBirth
            editUserFab.setOnClickListener {
                val action = UserDetailFragmentDirections.actionUserDetailFragmentToAddUserFragment(user.id)
                findNavController().navigate(action)
            }
        }
    }
}