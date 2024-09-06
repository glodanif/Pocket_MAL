package com.g.pocketmal.ui.legacy.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.g.pocketmal.data.keyvalue.SharingPatternDispatcher
import com.g.pocketmal.data.util.TitleType

import java.util.ArrayList

import com.g.pocketmal.databinding.FragmentSharingPatternsBinding
import com.g.pocketmal.transformedArgument

class SharingPatternsFragment : Fragment() {

    private val titleType: TitleType by transformedArgument<Int, TitleType>(EXTRA_TYPE, TitleType.ANIME) {
        TitleType.from(it)
    }

    private lateinit var dispatcher: SharingPatternDispatcher

    private val patternViews = ArrayList<com.g.pocketmal.ui.legacy.widget.SharingPatternView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dispatcher = SharingPatternDispatcher(requireActivity())
    }

    @SuppressLint("InflateParams")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val binding = FragmentSharingPatternsBinding.inflate(inflater, container, false)

        val sharingPatterns =  dispatcher.getSharingPatterns(titleType)

        sharingPatterns.forEach { pattern ->
            val patternView = com.g.pocketmal.ui.legacy.widget.SharingPatternView(activity, pattern)
            binding.llContainer.addView(patternView)
            patternViews.add(patternView)
        }

        return binding.root
    }

    fun savePatterns() {
        for (view in patternViews) {
            dispatcher.storePattern(view.key, view.value)
        }
    }

    companion object {

        private const val EXTRA_TYPE = "extra.type"

        fun newInstance(type: TitleType): SharingPatternsFragment {

            val fragment = SharingPatternsFragment()

            val bundle = Bundle()
            bundle.putInt(EXTRA_TYPE, type.type)
            fragment.arguments = bundle

            return fragment
        }
    }
}
