package com.example.whatsapp.presentation.HomePageLayout

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.whatsapp.R
import com.example.whatsapp.databinding.FragmentHomePageBinding
import com.example.whatsapp.presentation.HomePageLayout.Calls.CallsFragment
import com.example.whatsapp.presentation.HomePageLayout.Chat.ChatFragment
import com.example.whatsapp.presentation.HomePageLayout.Contacts.ContactsFragment
import com.example.whatsapp.presentation.HomePageLayout.Status.StatusFragment
import com.example.whatsapp.util.OutlineProvider
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

/**
 * HomePageFragment class.
 */
@AndroidEntryPoint
class HomePageFragment : Fragment() {
    private lateinit var binding: FragmentHomePageBinding

    val fragmentList = arrayListOf(
        ChatFragment(),
        CallsFragment(),
        StatusFragment()
    )

    // request contacts when fab is clicked
    private var requestReadContactsLauncher: ActivityResultLauncher<String?> =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {isGranted ->
            if (isGranted) {
                activity?.let {
                    it.supportFragmentManager.beginTransaction().add(R.id.fragmentContainer,ContactsFragment(),"contacts_fragment").commit()
                }
            } else {
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
                    launchContactsLauncherOnceAgain()
                }
            }
        }

    private fun launchContactsLauncherOnceAgain() {
        context?.let {
            AlertDialog.Builder(it)
                .setTitle("Permission needed")
                .setMessage("This app needs access to your contacts to function properly")
                .setPositiveButton("OK") {_, _ ->
                    requestReadContactsLauncher.launch(Manifest.permission.READ_CONTACTS)
                }
                .setNegativeButton("Cancel", null)
                .create()
                .show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomePageBinding.inflate(layoutInflater, container, false)
        binding.fabShowContacts.outlineProvider = OutlineProvider()
        binding.fabShowContacts.clipToOutline = true

        val window = requireActivity().window
        window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.Green1)
        binding.fabShowContacts.setOnClickListener {
            requestReadContactsLauncher.launch(Manifest.permission.READ_CONTACTS)
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViewPagerAndTabLayout()
    }

    private fun setUpViewPagerAndTabLayout() {
        binding.viewPager2.adapter = object : FragmentStateAdapter(childFragmentManager, lifecycle) {
            override fun getItemCount(): Int {
                return fragmentList.size
            }

            override fun createFragment(position: Int): Fragment {
                return fragmentList[position]
            }
        }

        // setup tabs
        TabLayoutMediator(binding.tabLayout, binding.viewPager2) {tab, position ->
            when(position) {
                0 -> tab.text = "Chats"
                1 -> tab.text = "Calls"
                2 -> tab.text = "Status"
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_search -> {
                // Handle search action
                true
            }

            R.id.menu_camera -> {
                // Handle camera action
                true
            }

            R.id.create_new_group -> {
                true
            }

            R.id.create_new_broadcast -> {
                true
            }

            R.id.payments -> {
                true
            }

            R.id.settings -> {
                true
            }

            R.id.starred_message -> {
                true
            }

            R.id.linked_devices -> {
                true
            }

            else -> {
                false
            }
        }
    }
}