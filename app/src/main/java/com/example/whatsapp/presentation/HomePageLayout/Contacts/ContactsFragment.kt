package com.example.whatsapp.presentation.HomePageLayout.Contacts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.whatsapp.R
import com.example.whatsapp.databinding.FragmentContactsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * ContactsFragment class.
 */
@AndroidEntryPoint
class ContactsFragment : Fragment(), IContactsView {

    private var binding: FragmentContactsBinding? = null
    private var deviceContacts: MutableList<String> = arrayListOf()

    private val contactsViewModel: ContactsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentContactsBinding.inflate(inflater, container, false)
        deviceContacts = ContactsManager(requireContext()).getContacts()

        deviceContacts.map {
            it.replace("+1", "").replace("","")
        }
        // Inflate the layout for this fragment
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.let {binding ->
            if (deviceContacts.isNotEmpty()) {
                val newList = ArrayList<String>()
                newList.add("000000")
                newList.add("1234567890")
                newList.add("8765")
                newList.add("12345")
                newList.add("1234")
                newList.add("1234567")
                newList.add("9876")
                contactsViewModel.getAllWhatsAppContacts(newList, this)

                var contactsAdapter = ContactsAdapter()
                binding.contactsRecyclerView.layoutManager = LinearLayoutManager(context)
                binding.contactsRecyclerView.adapter = contactsAdapter
                CoroutineScope(Dispatchers.IO).launch {
                    contactsViewModel.whatsAppContactsList.collectLatest {
                        withContext(Dispatchers.Main) {
                            if (it.isNotEmpty()) {
                                contactsAdapter.submitList(it)
                                binding.subtitleText.text = "${contactsAdapter.currentList.size} Contacts"
                            }
                        }
                    }
                }
            }
        }
    }

    override fun showError(error: String) {
        binding?.let {
            it.contactsProgressBar.visibility = View.GONE
        }
        Toast.makeText(context, error, Toast.LENGTH_LONG).show()
    }

    override fun showProgressBar() {
        binding?.let { it.contactsProgressBar.visibility = View.VISIBLE }
    }

    override fun hideProgressBar() {
        binding?.let { it.contactsProgressBar.visibility = View.GONE }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_contacts_fragment,menu)
    }
}