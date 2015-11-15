package com.github.chuross.qiiip.ui.fragment.screen

import android.os.Bundle
import android.view.ViewGroup
import com.github.chuross.chuross.qiiip.R
import com.github.chuross.qiiip.ui.fragment.BaseFragment
import com.github.chuross.qiiip.ui.fragment.FragmentTitlePagerAdapter
import com.github.chuross.qiiip.ui.fragment.presenter.HomeScreenFragmentPresenter
import com.github.chuross.qiiip.ui.fragment.template.HomeScreenFragmentTemplate
import com.github.chuross.qiiip.application.Screen as AppScreen

class HomeScreenFragment : BaseFragment<HomeScreenFragmentPresenter, HomeScreenFragmentTemplate>(), ScreenFragment {

    override fun getScreen(): Screen = AppScreen.HOME

    override fun getScreenExitAction(): ScreenExitAction = ScreenExitAction.HIDE

    override fun getScreenIdentity(): String = HomeScreenFragment::class.java.name

    override fun createPresenter(): HomeScreenFragmentPresenter = HomeScreenFragmentPresenter(this)

    override fun createTemplate(p0: ViewGroup?, p1: Bundle?): HomeScreenFragmentTemplate = HomeScreenFragmentTemplate(activity.applicationContext)

    override fun onViewCreated(template: HomeScreenFragmentTemplate, savedInstanceState: Bundle?) {
        super.onViewCreated(template, savedInstanceState)

        val toolbar = template.toolbar
        toolbar.title = getString(R.string.app_name)

        val viewPager = template.viewPager
        viewPager.adapter = FragmentTitlePagerAdapter(childFragmentManager, presenter.viewPagerList)
    }
}