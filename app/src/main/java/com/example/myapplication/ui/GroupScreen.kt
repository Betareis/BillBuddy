package ui

import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import model.Group

class GroupScreen : AppCompatActivity() {
    private lateinit var groupListLayout: LinearLayout

    private val groups = mutableListOf<Group>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_screen)

        groupListLayout = findViewById(R.id.group_list_layout)

        val addGroupButton: Button = findViewById(R.id.add_group_button)
        addGroupButton.setOnClickListener {
            addGroup(Group("New Group", "Description"))
        }
    }

    private fun addGroup(group: Group) {
        groups.add(group)
        displayGroups()
    }

    private fun displayGroups() {
        groupListLayout.removeAllViews()
        for (group in groups) {
            val groupButton = Button(this)
            groupButton.text = group.name
            groupButton.setOnClickListener {
                // Implement action when group button is clicked
            }
            groupListLayout.addView(groupButton)
        }
    }
}
