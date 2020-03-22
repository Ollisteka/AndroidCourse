package com.example.androidcourse

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_sheet.*


interface IHabitsObservable {
    fun addHabitsObserver(observer: IHabitsObserver)
}

class MainActivity : AppCompatActivity(), IHabitsObservable, NavigationView.OnNavigationItemSelectedListener {
    private lateinit var habitsPagerAdapter: HabitsListPagerAdapter
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private lateinit var mToolbar: Toolbar

    private val habitsWatchersByType: MutableMap<HabitType, IHabitsObserver> = mutableMapOf()
    private val model: HabitsViewModel by viewModels()

    private val dataSetChangedObserver = Observer<Any> {
        habitsWatchersByType[HabitType.Bad]?.notifyDataSetHasChanged()
        habitsWatchersByType[HabitType.Good]?.notifyDataSetHasChanged()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mToolbar = toolbar as Toolbar
        setSupportActionBar(mToolbar)

        habitsPagerAdapter = HabitsListPagerAdapter(this)
        pager.adapter = habitsPagerAdapter

        TabLayoutMediator(tab_layout, pager) { tab, position ->
            tab.text = if (position == 0) getString(R.string.tab_name_good) else getString(R.string.tab_name_bad)
        }.attach()

        addHabitButton.setOnClickListener {
            val sendIntent = Intent(applicationContext, EditHabitActivity::class.java)
            startActivity(sendIntent)
        }

        model.dataSetChanged.observe(this, dataSetChangedObserver)

        nameRadio.setOnCheckedChangeListener(model.nameSortListener)
        periodicityRadio.setOnCheckedChangeListener(model.periodicitySortListener)

        val sheetBehavior = BottomSheetBehavior.from(filterBottomSheet);
        bottom_sheet_header.setOnClickListener {
            if (sheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
                sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            } else if (sheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }

        searchEdit.doAfterTextChanged { text -> model.searchWord = text.toString() }
        searchEdit.setText(model.searchWord)

        drawerToggle = ActionBarDrawerToggle(this, navDrawerLayout, mToolbar, R.string.open_drawer, R.string.close_drawer)
        drawerToggle.isDrawerIndicatorEnabled = true
        navDrawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()
        navDrawer.setNavigationItemSelectedListener(this)
    }

    override fun addHabitsObserver(observer: IHabitsObserver) {
        habitsWatchersByType[observer.habitType] = observer
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)

        intent?.getParcelableExtra<Habit?>(EXTRA.NEW_HABIT)?.let { addOrUpdate(it) }
    }

    private fun addOrUpdate(newHabit: Habit) {
        val existingHabit = model.findById(newHabit.id)
        if (existingHabit != null) {
            val oldType = existingHabit.type
            if (oldType != newHabit.type) {
                habitsWatchersByType[oldType]?.onHabitDelete(existingHabit.id)
            }
        }
        model.addOrUpdate(newHabit)
        habitsWatchersByType[newHabit.type]?.onHabitEdit(newHabit.id)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item_about -> startActivity(Intent(applicationContext, AboutActivity::class.java))
        }

        navDrawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onDestroy() {
        super.onDestroy()

        model.dataSetChanged.removeObserver(dataSetChangedObserver)
    }
}
