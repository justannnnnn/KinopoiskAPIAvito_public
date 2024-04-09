package com.example.kinopoiskapiavito.presentation.main.filter


import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.DatePicker
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.kinopoiskapiavito.Constants.RESULT_ACCESS
import com.example.kinopoiskapiavito.Constants.RESULT_DENIED
import com.example.kinopoiskapiavito.Constants.atv_default
import com.example.kinopoiskapiavito.Constants.networkService
import com.example.kinopoiskapiavito.Constants.networkServiceAdd
import com.example.kinopoiskapiavito.Constants.rb_default
import com.example.kinopoiskapiavito.Constants.y1_default
import com.example.kinopoiskapiavito.Constants.y2_default
import com.example.kinopoiskapiavito.R
import com.example.kinopoiskapiavito.model.Country
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.System.currentTimeMillis
import java.time.LocalDate
import java.util.Calendar
import java.util.Date


class FilterActivity : AppCompatActivity() {

    val nServiceAdd = networkServiceAdd
    var countries: MutableList<String> = mutableListOf()
    lateinit var autoCompleteCountry: AutoCompleteTextView
    lateinit var mCalendar: Calendar


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_filter)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val radioGroup = findViewById<RadioGroup>(R.id.ratingRG)
        rb_default?.let { radioGroup.check(it) }
        autoCompleteCountry = findViewById<AutoCompleteTextView>(R.id.countryATV)
        autoCompleteCountry.setText(atv_default)

        nServiceAdd.getCountries().enqueue(object: Callback<List<Country>>{
            override fun onResponse(call: Call<List<Country>>, response: Response<List<Country>>) {
                if (response.isSuccessful){
                    for (country in (response.body() as List<Country>))
                        countries.add(country.name)
                    val countryAdapter = ArrayAdapter<String>(this@FilterActivity, android.R.layout.simple_dropdown_item_1line, countries)
                    autoCompleteCountry = findViewById<AutoCompleteTextView>(R.id.countryATV)
                    autoCompleteCountry.setAdapter(countryAdapter)
                    autoCompleteCountry.threshold = 2
                    if (countries.size > 0) {
                        autoCompleteCountry.onFocusChangeListener =
                            View.OnFocusChangeListener { _, hasFocus ->
                                if (hasFocus)
                                    autoCompleteCountry.showDropDown()
                            }
                    }
                }
            }

            override fun onFailure(call: Call<List<Country>>, t: Throwable) {

            }

        })

        mCalendar = Calendar.getInstance()
        val datePicker1 = findViewById<DatePicker>(R.id.startDP)
        setDatePicker(datePicker1)
        if (y1_default != null)
            datePicker1.updateDate(y1_default!!, 1, 1)


        val datePicker2 = findViewById<DatePicker>(R.id.endDP)
        setDatePicker(datePicker2)
        if (y2_default != null)
            datePicker1.updateDate(y2_default!!, 1, 1)

        /*datePicker1.setOnDateChangedListener { view, year, monthOfYear, dayOfMonth ->
        }
        datePicker2.setOnDateChangedListener{view, year, monthOfYear, dayOfMonth ->
            mCalendar.set(year, monthOfYear, dayOfMonth)
            datePicker1.maxDate = mCalendar.timeInMillis
        }
         */

        var rating: Int? = null
        var country: String? = null
        var year1: Int? = null
        var year2: Int? = null

        val applyButton = findViewById<Button>(R.id.applyButton)
        applyButton.setOnClickListener {
            rating = when (radioGroup.checkedRadioButtonId){
                R.id.rating0RB -> 0
                R.id.rating6RB -> 6
                R.id.rating12RB -> 12
                R.id.rating16RB -> 16
                R.id.rating18RB -> 18
                else -> null
            }
            country = autoCompleteCountry.text.toString()
            year1 = datePicker1.year
            year2 = datePicker2.year

            if (year1!! > year2!!){
                Toast.makeText(this, "Неверно задан временной период!", Toast.LENGTH_LONG).show()
            }
            else {
                var data = Intent()
                data.putExtra("rating", rating)
                rb_default = radioGroup.checkedRadioButtonId
                data.putExtra("country", country)
                atv_default = country.toString()
                data.putExtra("year1", year1)
                y1_default = year1 as Int
                data.putExtra("year2", year2)
                y2_default = year2 as Int
                setResult(RESULT_ACCESS, data)
                finish()
            }
        }
        val discardButton = findViewById<Button>(R.id.discardButton)
        discardButton.setOnClickListener {
            setResult(RESULT_DENIED)
            rb_default = null
            atv_default = ""
            y1_default = null
            y2_default = null
            finish()
        }


    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setDatePicker(dp: DatePicker){
        val yearPicker = (dp.getChildAt(0) as ViewGroup).getChildAt(0)
        yearPicker.visibility = View.VISIBLE
        (yearPicker as ViewGroup).getChildAt(0).visibility = View.GONE
        (yearPicker as ViewGroup).getChildAt(1).visibility = View.GONE

        dp.maxDate = currentTimeMillis()
    }
}