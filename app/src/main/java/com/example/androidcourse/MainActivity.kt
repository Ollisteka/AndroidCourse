package com.example.androidcourse

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*


private const val HABITS = "HABITS"

interface IHabitsProvider {
    fun getHabits(habitType: HabitType): List<Habit>
    fun addHabitsWatcher(watcher: IHabitsWatcher)
}

class MainActivity : AppCompatActivity(), IHabitsProvider, NavigationView.OnNavigationItemSelectedListener {
    private lateinit var habitsPagerAdapter: HabitsListPagerAdapter
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private lateinit var mToolbar: Toolbar

    private var habits: MutableList<Habit> = mutableListOf(
        Habit("Хорошая", "Описание", type = HabitType.Good),
        Habit("Плохая", "Описание", type = HabitType.Bad)
    )

    private val habitsWatchersByType: MutableMap<HabitType, IHabitsWatcher> = mutableMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mToolbar = toolbar as Toolbar
        setSupportActionBar(mToolbar)

        savedInstanceState?.getParcelableArray(HABITS)?.let { savedHabits ->
            habits = savedHabits.map { it as Habit }.toMutableList()
        }

        habitsPagerAdapter = HabitsListPagerAdapter(this)
        pager.adapter = habitsPagerAdapter

        TabLayoutMediator(tab_layout, pager) { tab, position ->
            tab.text = if (position == 0) getString(R.string.tab_name_good) else getString(R.string.tab_name_bad)
        }.attach()

        addHabitButton.setOnClickListener {
            val sendIntent = Intent(applicationContext, EditHabitActivity::class.java)
            startActivity(sendIntent)
        }
        drawerToggle = ActionBarDrawerToggle(this, navDrawerLayout, mToolbar, R.string.open_drawer, R.string.close_drawer)
        drawerToggle.isDrawerIndicatorEnabled = true
        navDrawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()
        navDrawer.setNavigationItemSelectedListener(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putParcelableArray(HABITS, habits.toTypedArray())
    }

    override fun addHabitsWatcher(watcher: IHabitsWatcher) {
        habitsWatchersByType[watcher.habitType] = watcher
    }

    override fun getHabits(habitType: HabitType): List<Habit> {
        return habits.filter { it.type == habitType }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)

        intent?.getParcelableExtra<Habit?>(EXTRA.NEW_HABIT)?.let { addOrUpdate(it) }
    }

    private fun addOrUpdate(newHabit: Habit) {
        val existingHabit = habits.withIndex().find { it.value.id == newHabit.id }
        if (existingHabit != null) {
            habits[existingHabit.index] = newHabit
            val oldType = existingHabit.value.type
            if (oldType != newHabit.type) {
                habitsWatchersByType[oldType]?.onHabitDelete(existingHabit.value.id)
            }
        } else {
            habits.add(newHabit)
        }
        habitsWatchersByType[newHabit.type]?.onHabitEdit(newHabit)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item_about -> startActivity(Intent(applicationContext, AboutActivity::class.java))
        }

        navDrawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
