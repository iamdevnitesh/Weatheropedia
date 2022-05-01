package com.imcodernitesh.weatheropedia.onboarding

import android.content.Intent
import android.graphics.Color
import android.graphics.PointF
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.imcodernitesh.weatheropedia.R
import com.imcodernitesh.weatheropedia.activities.HomeActivity
import com.imcodernitesh.weatheropedia.databinding.FragmentViewPagerBinding
import com.jem.concentriconboarding.ConcentricOnboardingViewPager

class ViewPagerFragment : Fragment() {
    private lateinit var binding: FragmentViewPagerBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentViewPagerBinding.inflate(inflater, container, false)
        binding.skipOnboarding.visibility = View.GONE
        // Inflate the layout for this fragment

        binding.skipOnboarding.setOnClickListener{
            findNavController().navigate(R.id.action_viewPagerFragment_to_homeActivity)
        }
        // set viewpager change listener and change the visibility of skipOnBoarding button if current page is last page
        binding.viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                if (position == binding.viewpager.adapter!!.count - 1) {
                    binding.skipOnboarding.visibility = View.VISIBLE
                } else {
                    binding.skipOnboarding.visibility = View.GONE
                }
            }
        })


        binding.modeSpinner.visibility = View.INVISIBLE

        binding.viewpager.adapter = CustomFragmentPagerAdapter(parentFragmentManager)

        // Create 20 times the number of actual pages, and start in the middle.
        // This way users can swipe left or right from the start.
        // Definitely not a good idea for production, but good enough for a demo app.
        binding.viewpager.setCurrentItem(titleArray.count() - titleArray.count() , false)

        binding.modeSpinner.adapter =
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                listOf("Slide Mode", "Reveal Mode")
            )
        binding.modeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val textView = (p0?.getChildAt(0) as? TextView?)
                textView?.setTextColor(Color.WHITE)
                binding.viewpager.mode = when (p2) {
                    1 -> ConcentricOnboardingViewPager.Mode.REVEAL
                    else -> ConcentricOnboardingViewPager.Mode.SLIDE
                }
            }
        }

        return binding.root
    }

    private fun updateViewPagerReveal(centerX: Float, centerY: Float, radius: Int) {
        binding.viewpager.revealCenterPoint = PointF(centerX, centerY)
        binding.viewpager.revealRadius = radius
    }
}