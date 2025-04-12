package com.develotter.calendarviewsample

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.develotter.calendarview.adapter.MonthSampleAdapter
import com.develotter.calendarview.enums.TypeArtCalender
import com.develotter.calendarview.enums.TypeSelectDay
import com.develotter.calendarview.enums.TypeViewCalender
import com.develotter.calendarview.enums.TypeWeekShow
import com.develotter.calendarview.georgian.GeorgianDayStatus
import com.develotter.calendarview.jalali.JalaliDayStatus
import com.develotter.calendarview.status.CalendarStatus
import com.develotter.calendarview.status.DayStatus
import com.develotter.calendarview.status.MonthStatus
import com.develotter.calendarview.status.SelectRangeDayStatus
import com.develotter.calendarviewsample.databinding.ActivityMainBinding
import com.develotter.calendarviewsample.databinding.DialogMultiBinding
import com.develotter.calendarviewsample.databinding.RowCalendarBinding
import com.develotter.calendarviewsample.databinding.RowCalendarControllerBinding
import com.develotter.calendarviewsample.databinding.RowMonthBinding
import com.github.eloyzone.jalalicalendar.JalaliDate
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale
@Suppress("UNCHECKED_CAST")
class CalendarViewActivity : AppCompatActivity() {

    var noOrYes = arrayOf("")
    private lateinit var binding: ActivityMainBinding
    private lateinit var thisCalendarStatus: CalendarStatus

    private lateinit var lcInUse :Locale


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        noOrYes = arrayOf(getString(R.string.No),getString(R.string.Yes))
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

         lcInUse =
            if (getCalendarLanguageEnumsWithBaseLanguage() == LanguageEnums.DefaultDevice) {
                when (getLanguageEnumsWithBaseLanguage()) {
                    LanguageEnums.DefaultDevice -> {
                        resources.getDeviceDefaultLocale()
                    }

                    else -> {
                        getLanguageLocale()
                    }

                }
            } else {
                when (getCalendarLanguageEnumsWithBaseLanguage()) {
                    LanguageEnums.DefaultDevice -> {
                        resources.getDeviceDefaultLocale()
                    }

                    else -> {
                        getCalendarLanguageLocale()
                    }

                }
            }

        thisCalendarStatus = CalendarStatus().setLocalInUse(lcInUse)
        thisCalendarStatus.setArtSelected(TypeArtCalender.JALALI)
        setUpCalendar()

        binding.showDialogChangeCalendarType.setOnClickListener {
            changeTypeOfCalendar()
        }
        binding.showDialogChangeCalendarViewType.setOnClickListener {
            changeViewCalendar()
        }
        binding.changeLocaleApp.setOnClickListener {
            changeLanguageApp()
        }
        binding.changeLocaleCalendar.setOnClickListener {
            changeLanguageCalendar()
        }
        binding.changeSelectDayType.setOnClickListener {
            changeSelectDayType()
        }
        binding.showLastMonth.setOnClickListener {
            changeLastMonthShowing()
        }
        binding.showNextMonth.setOnClickListener {
            changeNextMonthShowing()
        }
        binding.showMonthName.setOnClickListener {
            changeMonthNameShowing()
        }
        binding.showWeekName.setOnClickListener {
            changeWeekNameShowing()
        }

        binding.goThisMonth.setOnClickListener {
            binding.calendar.scrollToThisMonth()
        }
        binding.goToday.setOnClickListener {
            binding.calendar.scrollToToday()
        }
    }

    fun updateTitle() {

        binding.showDialogChangeCalendarViewTypeText.text =
            thisCalendarStatus.getViewTypeSelected().title.toString()
        binding.showDialogChangeCalendarTypeText.text =
            thisCalendarStatus.getArtSelected().title.toString()
         val  t =
            getLanguageEnumsWithBaseLanguage().title + " / " + resources.getDeviceDefaultLocale()
                .toLanguageTag()
        binding.changeLocaleAppText.text =t
        val  t2  =
            getCalendarLanguageEnumsWithBaseLanguage().title + " / " + thisCalendarStatus.getLocalInUse()
                .toString()
        binding.changeLocaleCalendarText.text = t2
        binding.changeSelectDayTypeText.text =
            thisCalendarStatus.getTypeSelectDay().title.toString()
        binding.showWeekNameText.text =   thisCalendarStatus.getShowRowWeekName().title.toString()
        binding.showMonthNameText.text = noOrYes[     if(thisCalendarStatus.getShowRowMonthName()) 1 else 0]
        binding.showNextMonthText.text = noOrYes[     if(thisCalendarStatus.getShowNextMonth()) 1 else 0]
        binding.showLastMonthText.text = noOrYes[     if(thisCalendarStatus.getShowLastMonth()) 1 else 0]
        binding.toolbar.post {
            binding.toolbar.title = binding.calendar.getThisMonthCaption()
        }
    }

      fun  setUpCalendar() {

        when (thisCalendarStatus.getArtSelected()) {
            TypeArtCalender.JALALI -> {


                setUpJalaliCalendar()
            }

            TypeArtCalender.Georgian -> {



                setUpGeorgian()
            }
        }
        updateTitle()
    }

     fun  setUpJalaliCalendar() {
         var dayStatusListSelectedBySingleSelect: MutableList<JalaliDayStatus> = mutableListOf()
         val dayStatus = JalaliDayStatus()
         dayStatus.setDateSelect(1404,1,5)
         dayStatusListSelectedBySingleSelect.add(0, dayStatus )
        binding.calendar.addMonths(object :
            MonthSampleAdapter<JalaliDate, JalaliDayStatus, RowCalendarBinding, RowCalendarBinding, RowMonthBinding>
                (thisCalendarStatus, dayStatusListSelectedBySingleSelect = dayStatusListSelectedBySingleSelect ) {
            override fun onBindWeekView(dayStatus: DayOfWeek): RowCalendarBinding {
                val binding = RowCalendarBinding.inflate(layoutInflater)
                binding.textRow.text =
                    dayStatus.getDisplayName(TextStyle.SHORT, thisCalendarStatus.getLocalInUse())

                return binding
            }


            override fun onBindDayView(dayStatus: JalaliDayStatus): RowCalendarBinding {
                val binding = RowCalendarBinding.inflate(layoutInflater)
                binding.textRow.text = dayStatus.formatDayIntWithLocale()

                animateDaySelection(dayStatus,binding)

                return binding
            }

            override fun onBindControllerCalendar(): ViewBinding {

                val bindings = RowCalendarControllerBinding.inflate(layoutInflater)
                bindings.nextButton.setOnClickListener {
                    binding.calendar.scrollToNextMonth()
                }
                bindings.prevButton.setOnClickListener {
                    binding.calendar.scrollToPreviousMonth()
                }
                return bindings
            }

            override fun onBindMonthView(dayStatus: MonthStatus<*, *>): RowMonthBinding {
                val binding = RowMonthBinding.inflate(layoutInflater)
                binding.textRowMonth.text = dayStatus.getThisMonthCaption()


                return binding
            }


            override fun onBindLastMonthDayView(dayStatus: JalaliDayStatus): RowCalendarBinding {
                val binding = RowCalendarBinding.inflate(layoutInflater)

                binding.textRow.text = dayStatus.formatDayIntWithLocale()
                binding.textRow.setTextColor(
                    ContextCompat.getColor(
                        this@CalendarViewActivity,
                        R.color.gray
                    )
                )
                return binding
            }

            override fun onBindNextMonthDayView(dayStatus: JalaliDayStatus): RowCalendarBinding {
                val binding = RowCalendarBinding.inflate(layoutInflater)
                binding.textRow.text = dayStatus.formatDayIntWithLocale()
                binding.textRow.setTextColor(
                    ContextCompat.getColor(
                        this@CalendarViewActivity,
                        R.color.gray
                    )
                )
                return binding
            }

            override fun onDayFocused(status: MonthStatus<*, JalaliDayStatus>) {

            }


            override fun onDayItemClick(
                dayStatus: JalaliDayStatus,
                viewbinding: RowCalendarBinding
            ) {
                animateDaySelection(dayStatus, viewbinding)
            }


            override fun onMonthViewActive(
                monthStatus: MonthStatus<*, *>,
                headerPager: RowMonthBinding
            ) {
                headerPager.textRowMonth.text = monthStatus.getThisMonthCaption()
                binding.toolbar.title = monthStatus.getThisMonthCaption()
            }
        })

    }

    fun setUpGeorgian() {

        var dayStatusListSelectedBySingleSelect: MutableList<GeorgianDayStatus> = mutableListOf()
        val dayStatus = GeorgianDayStatus()
        dayStatus.setDateSelect(2025,4,5)
        dayStatusListSelectedBySingleSelect.add(0, dayStatus )

        binding.calendar.addMonths(object :
            MonthSampleAdapter<LocalDate, GeorgianDayStatus, RowCalendarBinding, RowCalendarBinding, RowMonthBinding>
                (thisCalendarStatus, dayStatusListSelectedBySingleSelect = dayStatusListSelectedBySingleSelect) {
            override fun onBindWeekView(dayStatus: DayOfWeek): RowCalendarBinding {
                val binding = RowCalendarBinding.inflate(layoutInflater)
                binding.textRow.text =
                    dayStatus.getDisplayName(TextStyle.SHORT, thisCalendarStatus.getLocalInUse())

                return binding
            }


            override fun onBindDayView(dayStatus: GeorgianDayStatus): RowCalendarBinding {
                val binding = RowCalendarBinding.inflate(layoutInflater)
                binding.textRow.text = dayStatus.formatDayIntWithLocale()

                animateDaySelection(dayStatus, binding)
                return binding
            }

            override fun onBindControllerCalendar(): ViewBinding {
                val bindings = RowCalendarControllerBinding.inflate(layoutInflater)
                bindings.nextButton.setOnClickListener {
                    binding.calendar.scrollToNextMonth()
                }
                bindings.prevButton.setOnClickListener {
                    binding.calendar.scrollToPreviousMonth()
                }
                return bindings
            }

            override fun onBindMonthView(dayStatus: MonthStatus<*, *>): RowMonthBinding {
                val binding = RowMonthBinding.inflate(layoutInflater)
                binding.textRowMonth.text = dayStatus.getThisMonthCaption()


                return binding
            }

            override fun onBindLastMonthDayView(dayStatus: GeorgianDayStatus): RowCalendarBinding {
                val binding = RowCalendarBinding.inflate(layoutInflater)

                binding.textRow.text = dayStatus.formatDayIntWithLocale()
                binding.textRow.setTextColor(
                    ContextCompat.getColor(
                        this@CalendarViewActivity,
                        R.color.gray
                    )
                )
                return binding
            }

            override fun onBindNextMonthDayView(dayStatus: GeorgianDayStatus): RowCalendarBinding {
                val binding = RowCalendarBinding.inflate(layoutInflater)
                binding.textRow.text = dayStatus.formatDayIntWithLocale()

                binding.textRow.setTextColor(
                    ContextCompat.getColor(
                        this@CalendarViewActivity,
                        R.color.gray
                    )
                )
                return binding
            }

            override fun onDayFocused(status: MonthStatus<*, GeorgianDayStatus>) {
                TODO("Not yet implemented")
            }


            override fun onDayItemClick(
                dayStatus: GeorgianDayStatus,
                viewbinding: RowCalendarBinding
            ) {

                animateDaySelection(dayStatus, viewbinding)

            }

            override fun onMonthViewActive(
                monthStatus: MonthStatus<*, *>,
                headerPager: RowMonthBinding
            ) {
                headerPager.textRowMonth.text = monthStatus.getThisMonthCaption()
                binding.toolbar.title = monthStatus.getThisMonthCaption()
            }
        })
    }

    private fun animateDaySelection(dayStatus: DayStatus<*>, viewbinding: RowCalendarBinding) {
        if (dayStatus.isToday()) {
            viewbinding.baseRow.setBackgroundColor(
                ContextCompat.getColor(
                    this@CalendarViewActivity,
                    R.color.white
                )
            )
            viewbinding.textRow.setTextColor(
                ContextCompat.getColor(
                    this@CalendarViewActivity,
                    com.develotter.calendarview.R.color.teal_700
                )
            )
        } else {
            viewbinding.baseRow.setBackgroundColor(
                ContextCompat.getColor(
                    this@CalendarViewActivity,
                    R.color.white
                )
            )
            viewbinding.textRow.setTextColor(
                ContextCompat.getColor(
                    this@CalendarViewActivity,
                    R.color.black
                )
            )
        }
        val startBackgroundColor = ContextCompat.getColor(
            this@CalendarViewActivity,
            if (dayStatus.isChecked) R.color.white else com.develotter.calendarview.R.color.teal_700
        )
        val endBackgroundColor = ContextCompat.getColor(
            this@CalendarViewActivity,
            if (dayStatus.isChecked) com.develotter.calendarview.R.color.teal_700 else R.color.white
        )
        val backgroundColorAnimator =
            ValueAnimator.ofObject(ArgbEvaluator(), startBackgroundColor, endBackgroundColor)
        backgroundColorAnimator.duration = 400
        backgroundColorAnimator.addUpdateListener { animator ->
            viewbinding.baseRow.setBackgroundColor(animator.animatedValue as Int)
        }

        val startTextColor = ContextCompat.getColor(
            this@CalendarViewActivity,
            if (dayStatus.isChecked) R.color.black else R.color.white
        )
        val endTextColor = ContextCompat.getColor(
            this@CalendarViewActivity,
            if (dayStatus.isChecked) R.color.white else R.color.black
        )
        val textColorAnimator =
            ValueAnimator.ofObject(ArgbEvaluator(), startTextColor, endTextColor)
        textColorAnimator.duration = 400
        textColorAnimator.addUpdateListener { animator ->
            viewbinding.textRow.setTextColor(animator.animatedValue as Int)
        }
        backgroundColorAnimator.start()
        textColorAnimator.start()
        when (dayStatus.selectRangeDayStatus) {
            SelectRangeDayStatus.Nothing -> {
                viewbinding.imgRowEndBottom.visibility = View.GONE
                viewbinding.imgRowEndTop.visibility = View.GONE
                viewbinding.imgRowStartTop.visibility = View.GONE
                viewbinding.imgRowStartBottom.visibility = View.GONE
            }

            SelectRangeDayStatus.AsStartOrEnd -> {
                viewbinding.imgRowEndBottom.visibility = View.VISIBLE
                viewbinding.imgRowEndTop.visibility = View.VISIBLE
                viewbinding.imgRowStartTop.visibility = View.VISIBLE
                viewbinding.imgRowStartBottom.visibility = View.VISIBLE
            }

            SelectRangeDayStatus.AsStart -> {
                viewbinding.imgRowEndBottom.visibility = View.GONE
                viewbinding.imgRowEndTop.visibility = View.GONE
                viewbinding.imgRowStartTop.visibility = View.VISIBLE
                viewbinding.imgRowStartBottom.visibility = View.VISIBLE
            }

            SelectRangeDayStatus.AsEnd -> {
                viewbinding.imgRowEndBottom.visibility = View.VISIBLE
                viewbinding.imgRowEndTop.visibility = View.VISIBLE
                viewbinding.imgRowStartTop.visibility = View.GONE
                viewbinding.imgRowStartBottom.visibility = View.GONE
            }
        }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
//            R.id.action_settings -> {
//
//
//                true
//            }
//            R.id.action_art -> {
//
//
//                true
//            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun changeViewCalendar() {
        val bindingDialog = DialogMultiBinding.inflate(layoutInflater)
        val fe = TypeViewCalender.entries.map { it.title }
        AlertDialog.Builder(this)
            .setTitle("Please Select Type View of Calendar")
            .setView(bindingDialog.root)
            .setSingleChoiceItems(
                fe.toTypedArray(),
                thisCalendarStatus.getViewTypeSelected().ordinal,
                DialogInterface.OnClickListener { dialog, which ->
                    thisCalendarStatus.setViewTypeSelected(TypeViewCalender.entries[which])
                    dialog.dismiss()
                    setUpCalendar()
                })

            .show()
    }

    fun changeTypeOfCalendar() {
        val bindingDialog = DialogMultiBinding.inflate(layoutInflater)
        val fe = TypeArtCalender.entries.map { it.title }
        AlertDialog.Builder(this)
            .setTitle("Please Select Type of Calendar")
            .setView(bindingDialog.root)
            .setSingleChoiceItems(
                fe.toTypedArray(),
                thisCalendarStatus.getArtSelected().ordinal,
                DialogInterface.OnClickListener { dialog, which ->

                    thisCalendarStatus.setArtSelected(TypeArtCalender.entries[which])
                    dialog.dismiss()
                    setUpCalendar()
                })

            .show()
    }

    fun changeLanguageApp() {
        val bindingDialog = DialogMultiBinding.inflate(layoutInflater)


        val fe = LanguageEnums.entries.map { it.title }
        AlertDialog.Builder(this)
            .setTitle("Please Select Language of App")
            .setView(bindingDialog.root)
            .setSingleChoiceItems(
                fe.toTypedArray(),
                getLanguageEnumsWithBaseLanguage().ordinal,
                DialogInterface.OnClickListener { dialog, which ->
                    if (which > 0) {
                        setLanguage(LanguageEnums.entries[which].title)
                    } else {
                        setLanguage("0")
                    }


                    dialog.dismiss()
                    restartApp()
                })

            .show()
    }

    fun changeLanguageCalendar() {
        val bindingDialog = DialogMultiBinding.inflate(layoutInflater)


        val fe = LanguageEnums.entries.map { it.title }
        AlertDialog.Builder(this)
            .setTitle("Please Select Language of Calendar")
            .setView(bindingDialog.root)
            .setSingleChoiceItems(
                fe.toTypedArray(),
                getCalendarLanguageEnumsWithBaseLanguage().ordinal,
                DialogInterface.OnClickListener { dialog, which ->
                    if (which > 0) {
                        setCalendarLanguage(LanguageEnums.entries[which].title)
                    } else {
                        setCalendarLanguage("0")
                    }
                    thisCalendarStatus.setLocalInUse(getCalendarLanguageLocale())
                    dialog.dismiss()
                    setUpCalendar()
                })

            .show()
    }
    fun changeSelectDayType() {
        val bindingDialog = DialogMultiBinding.inflate(layoutInflater)


        val fe = TypeSelectDay.entries.map { it.title }
        AlertDialog.Builder(this)
            .setTitle("Please Select Language of Calendar")
            .setView(bindingDialog.root)
            .setSingleChoiceItems(
                fe.toTypedArray(),
                thisCalendarStatus.getTypeSelectDay().ordinal,
                DialogInterface.OnClickListener { dialog, which ->

                    thisCalendarStatus.setTypeSelectDay(TypeSelectDay.entries[which])
                    dialog.dismiss()
                    setUpCalendar()
                })

            .show()
    }
    fun changeLastMonthShowing() {
        val bindingDialog = DialogMultiBinding.inflate(layoutInflater)



        AlertDialog.Builder(this)
            .setTitle(getString(R.string.show_last_month))
            .setView(bindingDialog.root)
            .setSingleChoiceItems(
                noOrYes,
               if(thisCalendarStatus.getShowLastMonth()) 1 else 0,
                DialogInterface.OnClickListener { dialog, which ->

                    thisCalendarStatus.setShowLastMonth(which==1)
                    dialog.dismiss()
                    setUpCalendar()
                })

            .show()
    }
    fun changeNextMonthShowing() {
        val bindingDialog = DialogMultiBinding.inflate(layoutInflater)



        AlertDialog.Builder(this)
            .setTitle(getString(R.string.show_next_month))
            .setView(bindingDialog.root)
            .setSingleChoiceItems(
                noOrYes,
                if(thisCalendarStatus.getShowNextMonth()) 1 else 0,
                DialogInterface.OnClickListener { dialog, which ->

                    thisCalendarStatus.setShowNextMonth(which==1)
                    dialog.dismiss()
                    setUpCalendar()
                })

            .show()
    }
    fun changeWeekNameShowing() {

        val bindingDialog = DialogMultiBinding.inflate(layoutInflater)


        val fe = TypeWeekShow.entries.map { it.title }
        AlertDialog.Builder(this)
            .setTitle("Please Select Type of WeekShow")
            .setView(bindingDialog.root)
            .setSingleChoiceItems(
                fe.toTypedArray(),
                thisCalendarStatus.getShowRowWeekName().ordinal,
                DialogInterface.OnClickListener { dialog, which ->

                    thisCalendarStatus.setShowRowWeekName(TypeWeekShow.entries[which])
                    dialog.dismiss()
                    setUpCalendar()
                })

            .show()
    }
    fun changeMonthNameShowing() {
        val bindingDialog = DialogMultiBinding.inflate(layoutInflater)



        AlertDialog.Builder(this)
            .setTitle(getString(R.string.show_month_name))
            .setView(bindingDialog.root)
            .setSingleChoiceItems(
                noOrYes,
                if(thisCalendarStatus.getShowRowMonthName()) 1 else 0,
                DialogInterface.OnClickListener { dialog, which ->

                    thisCalendarStatus.setShowRowMonthName(which==1)
                    dialog.dismiss()
                    setUpCalendar()
                })

            .show()
    }


}