package com.example.userdatabase.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.userdatabase.BaseApplication
import com.example.userdatabase.databinding.FragmentUserListBinding
import com.example.userdatabase.ui.adapter.UserListAdapter
import com.example.userdatabase.viewmodel.UserViewModel
import com.example.userdatabase.viewmodel.UserViewModelFactory

class UserListFragment : Fragment() {

    private val viewModel: UserViewModel by activityViewModels {
        UserViewModelFactory(
            (activity?.application as BaseApplication).database.userDao()
        )
    }

    private var _binding: FragmentUserListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = UserListAdapter { user ->
            val action = UserListFragmentDirections
                .actionUserListFragmentToUserDetailFragment(user.id)
            findNavController().navigate(action)
        }

        viewModel.allUsers.observe(this.viewLifecycleOwner) {
            users -> users.let{adapter.submitList(it)}
        }

        binding.apply {
            recyclerView.adapter = adapter
            addUserFab.setOnClickListener {
                val action = UserListFragmentDirections.actionUserListFragmentToAddUserFragment()
                findNavController().navigate(action)
            }
        }
    }
}