package com.example.getitdone.ui

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.getitdone.R
import com.example.getitdone.data.model.TaskList
import com.example.getitdone.databinding.ActivityMainBinding
import com.example.getitdone.databinding.DialogAddTaskBinding
import com.example.getitdone.databinding.DialogAddTaskListBinding
import com.example.getitdone.databinding.TabButtonBinding
import com.example.getitdone.ui.tasks.StarredTasksFragment
import com.example.getitdone.ui.tasks.TasksFragment
import com.example.getitdone.util.InputValidator
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private var currentTaskLists: List<TaskList> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            viewModel.getTaskLists().collectLatest { taskLists ->

                currentTaskLists = taskLists


                // Use the renamed adapter
                binding.pager.adapter = PagerAdapter(this@MainActivity, taskLists)
                binding.pager.currentItem = 1

                enableEdgeToEdge()

                TabLayoutMediator(binding.tabs, binding.pager) { tab, position ->
                    when (position) {
                        0 -> tab.icon = ContextCompat.getDrawable(
                            this@MainActivity,
                            R.drawable.ic_star_filled
                        )

                        taskLists.size + 1 -> {
                            val buttonBinding = TabButtonBinding.inflate(layoutInflater)
                            tab.customView = buttonBinding.root.apply {
                                setOnClickListener { showAddTaskListDialog() }
                            }
                        }


                        else -> tab.text = taskLists[position - 1].name
                    }
                }.attach()
            }
        }

        binding.fab.setOnClickListener { showAddTaskDialog() }
    }

    private fun showAddTaskListDialog() {
        val dialogBinding = DialogAddTaskListBinding.inflate(layoutInflater)
        MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.add_new_list_button_title))
            .setView(dialogBinding.root)
            .setPositiveButton(getString(R.string.create_text)) { dialog, _ ->
                viewModel.addNewTaskList(dialogBinding.editTextListName.text.toString())
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.cancel_text)) { dialog, _ ->
                dialog.dismiss()

            }
            .show()


    }

    // Moved showAddTaskDialog to be a member function of MainActivity
    private fun showAddTaskDialog() {
        DialogAddTaskBinding.inflate(layoutInflater).apply { // Start of apply block

            val dialog = BottomSheetDialog(this@MainActivity)
            dialog.setContentView(root)

            buttonSave.isEnabled = false

            editTextTaskTitle.addTextChangedListener { input ->
                buttonSave.isEnabled = InputValidator.isInputValid(input?.toString())

            }

            buttonShowDetails.setOnClickListener {
                editTextTaskDetails.visibility =
                    if (editTextTaskDetails.visibility == View.VISIBLE) View.GONE else View.VISIBLE
            }

            binding.pager.currentItem

            buttonSave.setOnClickListener {

                val selectedTaskListId = currentTaskLists[binding.pager.currentItem - 1].id

                viewModel.createTask(
                    title = editTextTaskTitle.text.toString(),
                    description = editTextTaskDetails.text.toString(),
                    listId = selectedTaskListId
                )

                dialog.dismiss()
            }
            dialog.show()

        } // End of apply block
    }


    // Renamed PagerAdapter to MyFragmentStateAdapter
    class PagerAdapter(activity: FragmentActivity, private val taskLists: List<TaskList>) :
        FragmentStateAdapter(activity) {

        override fun getItemCount() = taskLists.size + 2

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> StarredTasksFragment()
                taskLists.size + 1 -> Fragment()
                else -> TasksFragment(taskLists[position - 1].id)
            }
        }
    }
}