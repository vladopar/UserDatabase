package com.example.userdatabase.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.userdatabase.BaseApplication
import com.example.userdatabase.databinding.FragmentAddUserBinding
import com.example.userdatabase.model.User
import com.example.userdatabase.viewmodel.UserViewModel
import com.example.userdatabase.viewmodel.UserViewModelFactory

class AddUserFragment : Fragment() {

    private val navigationArgs: AddUserFragmentArgs by navArgs()

    private var _binding: FragmentAddUserBinding? = null
    private val binding get() = _binding!!

    private lateinit var user: User

    private val viewModel: UserViewModel by activityViewModels {
        UserViewModelFactory(
            (activity?.application as BaseApplication).database.userDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = navigationArgs.id

        if (id > 0) {
            viewModel.getUser(id).observe(this.viewLifecycleOwner) {
                user = it
                bindUser(user)
            }
            binding.deleteButton.visibility = View.VISIBLE
            binding.deleteButton.setOnClickListener {
                deleteUser(user)
            }

        } else {
            binding.saveButton.setOnClickListener {
                addUser()
            }
        }

    }

    private fun isValidEntry() = viewModel.isValidEntry(
        binding.firstNameInput.text.toString(),
        binding.lastNameInput.text.toString()
    )

    private fun addUser() {
        if (isValidEntry()) {
            viewModel.addUser(
                binding.firstNameInput.text.toString(),
                binding.lastNameInput.text.toString()
            )
            findNavController().navigate(AddUserFragmentDirections.actionAddUserFragmentToUserListFragment())
        }
    }

    private fun deleteUser(user: User) {
        viewModel.deleteUser(user)
        findNavController().navigate(AddUserFragmentDirections.actionAddUserFragmentToUserListFragment())
    }

    private fun updateUser() {
        if (isValidEntry()) {
            viewModel.updateUser(
                id = navigationArgs.id,
                firstName = binding.firstNameInput.text.toString(),
                lastName = binding.lastNameInput.text.toString()
            )
            findNavController().navigate(AddUserFragmentDirections.actionAddUserFragmentToUserListFragment())
        }
    }

    private fun bindUser(user: User) {
        binding.apply {
            firstNameInput.setText(user.firstName, TextView.BufferType.SPANNABLE)
            lastNameInput.setText(user.lastName, TextView.BufferType.SPANNABLE)
            saveButton.setOnClickListener {
                updateUser()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}