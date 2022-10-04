package com.muath.swipe2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.R
import com.muath.swipe2.databinding.ActivityMainBinding
import com.simform.refresh.SSPullToRefreshLayout
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding :ActivityMainBinding
    var data = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        for (i in 0 until 30){
            data.add(i.toString())
        }
        binding.rv.adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item,data)
        setupPullToRefresh()
    }
    private fun setupPullToRefresh(){
        binding.ssPullRefresh.setRepeatCount(SSPullToRefreshLayout.RepeatCount.INFINITE)
        binding.ssPullRefresh.setRefreshView(WaveAnimation(this))
        binding.ssPullRefresh.setRefreshStyle(SSPullToRefreshLayout.RefreshStyle.FLOAT)
        binding.ssPullRefresh.setRefreshViewParams(ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,300))
        //binding.ssPullRefresh.setRefreshTargetOffset(0f)
       // binding.ssPullRefresh.setRefreshInitialOffset(0f)
        binding.ssPullRefresh.setOnRefreshListener(object : SSPullToRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                // This is demo code to perform
                GlobalScope.launch {
                    delay(3000)
                    binding.ssPullRefresh.setRefreshing(false) // This line stops layout refreshing
                    MainScope().launch {
                        Toast.makeText(this@MainActivity,"Refresh Complete",Toast.LENGTH_SHORT).show()
                    }
                }
            }

        })
    }
}
