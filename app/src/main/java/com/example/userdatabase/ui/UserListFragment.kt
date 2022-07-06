package com.example.userdatabase.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.userdatabase.BaseApplication
import com.example.userdatabase.R
import com.example.userdatabase.databinding.FragmentUserListBinding
import com.example.userdatabase.ui.adapter.UserListAdapter
import com.example.userdatabase.viewmodel.UserViewModel
import com.example.userdatabase.viewmodel.UserViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class UserListFragment : Fragment() {

    private val viewModel: UserViewModel by activityViewModels {
        UserViewModelFactory(
            (activity?.application as BaseApplication).database.userDao()
        )
    }

    private var _binding: FragmentUserListBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.overflow_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_all -> {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(R.string.delete_all)
                    .setMessage(R.string.delete_all_message)
                    .setPositiveButton("Yes") { dialog, which ->
                        viewModel.deleteAll()
                    }
                    .setNegativeButton("No") { dialog, which -> }
                    .show()
                true
            }
            R.id.about -> {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(R.string.app_name)
                    .setMessage(R.string.version)
                    .setPositiveButton(R.string.ok) { dialog, which ->

                    }
                    .show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = UserListAdapter { user ->
            val action = UserListFragmentDirections
                .actionUserListFragmentToUserDetailFragment(user.id)
            findNavController().navigate(action)
        }

        viewModel.allUsers.observe(this.viewLifecycleOwner) { users ->
            users.let { adapter.submitList(it) }
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