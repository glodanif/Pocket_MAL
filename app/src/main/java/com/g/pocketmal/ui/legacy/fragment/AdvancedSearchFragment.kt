package com.g.pocketmal.ui.legacy.fragment

import androidx.fragment.app.Fragment

class AdvancedSearchFragment : Fragment() {

    /*private lateinit var binding: FragmentAdvancedBinding

    private val selectedColor: Int by lazy {
        requireActivity().resources.getColor(R.color.main)
    }
    private val unselectedColor: Int by lazy {
        requireActivity().resources.getColor(R.color.unselected)
    }

    private var startDate = DateHelper.UNKNOWN_DATE
    private var finishDate = DateHelper.getMalCurrentDateFormat(1)

    private var activity: SkeletonActivity? = null

    private var type: TitleType? = null

    var listener: ((SearchParams) -> Unit)? = null

    var searchParams = SearchParams()
        private set
    private var isRunAsChild = false

    private val startDateSetListener = OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

        if (view == null) {
            startDate = DateHelper.UNKNOWN_DATE
            binding.tvStartDate.setText(R.string.none)
            binding.tvStartDate.background.setColorFilter(unselectedColor, PorterDuff.Mode.SRC_ATOP)
        } else {
            startDate = DateHelper.getMalDateFormat(year, monthOfYear, dayOfMonth)
            try {
                binding.tvStartDate.text = DateHelper.VIEW_FORMAT.format(
                        Date(DateHelper.MAL_FORMAT.parse(startDate).time))
                binding.tvStartDate.background.setColorFilter(selectedColor, PorterDuff.Mode.SRC_ATOP)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        }
    }

    private val finishDateSetListener = OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

        if (view == null) {
            finishDate = DateHelper.getMalCurrentDateFormat()
            binding.tvFinishDate.setText(R.string.none)
            binding.tvFinishDate.background.setColorFilter(unselectedColor, PorterDuff.Mode.SRC_ATOP)
        } else {
            finishDate = DateHelper.getMalDateFormat(year, monthOfYear, dayOfMonth)
            try {
                binding.tvFinishDate.text = DateHelper.VIEW_FORMAT.format(
                        Date(DateHelper.MAL_FORMAT.parse(finishDate).time))
                binding.tvFinishDate.background.setColorFilter(selectedColor, PorterDuff.Mode.SRC_ATOP)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.activity = getActivity() as SkeletonActivity?
    }

    @SuppressLint("InflateParams")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentAdvancedBinding.inflate(inflater, container, false)

        binding.etQuery.addTextChangedListener(object : SimpleTextWatcher() {
            override fun afterTextChanged(string: String) {
                binding.tilQuery.error = null
            }
        })

        binding.tvStartDate.setOnClickListener {
            editStartDate()
        }
        binding.tvFinishDate.setOnClickListener {
            editFinishDate()
        }
        binding.tvSearchButton.setOnClickListener {
            search()
        }
        binding.cbReverse.setOnCheckedChangeListener { _, value ->
            searchParams.setReverse(value)
        }
        binding.cbExclude.setOnCheckedChangeListener { _, value ->
            searchParams.setExclude(value)
        }

        return binding.root
    }

    private fun setupFilters() {

        val typeList = listOf(*activity!!.resources.getStringArray(
                if (type === TitleType.ANIME) R.array.search__anime_types else R.array.search__manga_types))
        binding.hvTypeSelector.setData(typeList)
        binding.hvTypeSelector.addOnTagSelectListener { item, selected -> searchParams.type = if (selected) typeList.indexOf(item.toString()) + 1 else 0 }

        val statusList = listOf(*activity!!.resources.getStringArray(
                if (type === TitleType.ANIME) R.array.search__anime_statuses else R.array.search__manga_statuses))
        binding.hvStatusSelector.setData(statusList)
        binding.hvStatusSelector.addOnTagSelectListener { item, selected ->
            binding.etQuery.clearFocus()
            searchParams.status = if (selected) statusList.indexOf(item.toString()) + 1 else 0
        }

        val scoreList = listOf("10", "9", "8", "7", "6", "5", "4", "3", "2", "1")
        binding.hvScoreSelector.setData(scoreList)
        binding.hvScoreSelector.addOnTagSelectListener { item, selected ->
            binding.etQuery.clearFocus()
            searchParams.score = if (selected) 10 - scoreList.indexOf(item.toString()) else 0
        }

        if (type === TitleType.ANIME) {
            val ratingList = listOf(*activity!!.resources.getStringArray(R.array.search__anime_ratings))
            binding.hvRatingSelector.setData(ratingList)
            binding.hvRatingSelector.addOnTagSelectListener { item, selected ->
                binding.etQuery.clearFocus()
                searchParams.rated = if (selected) ratingList.indexOf(item.toString()) + 1 else 0
            }
        } else {
            binding.llRatedHolder.visibility = View.GONE
        }

        val genresList = listOf(*activity!!.resources.getStringArray(
                if (type === TitleType.ANIME) R.array.search__anime_genres else R.array.search__manga_genres))
        binding.hvGenresSelector.setData(genresList)
        binding.hvGenresSelector.addOnTagSelectListener { item, selected ->
            binding.etQuery.clearFocus()
            val genre = DataInterpreter.getGenreId(item.toString(), type!!)
            if (selected) {
                searchParams.addGenre(genre)
            } else {
                searchParams.removeGenre(genre)
            }
        }

        binding.hvGenresSelector.post {
            if (searchParams.genres.size == 1) {
                binding.hvGenresSelector.selectByValue(
                        DataInterpreter.getGenreName(searchParams.genres[0], type!!))
            }
        }

        binding.tvPeriod.setText(if (type === TitleType.ANIME)
            R.string.search__airing_period
        else
            R.string.search__publishing_period)

        val sortingList = Arrays.asList(*activity!!.resources.getStringArray(
                if (type === TitleType.ANIME) R.array.search__anime_sorting else R.array.search__manga_sorting))
        binding.hvSortingSelector.setData(sortingList)
        binding.hvSortingSelector.addOnTagSelectListener { item, selected ->
            binding.etQuery.clearFocus()
            if (!selected) {
                searchParams.sorting = 0
            } else {
                when (sortingList.indexOf(item.toString())) {
                    0 -> searchParams.sorting = if (type === TitleType.ANIME)
                        SearchParams.SEARCH_TYPE_ANIME
                    else
                        SearchParams.SEARCH_TYPE_MANGA
                    1 -> searchParams.sorting = SearchParams.SEARCH_EPISODES
                    2 -> searchParams.sorting = SearchParams.SEARCH_SCORE
                }
            }
        }

        binding.hvSortingSelector.post {

            if (searchParams.sorting > 1) {

                when (searchParams.sorting) {
                    SearchParams.SEARCH_TYPE_ANIME, SearchParams.SEARCH_TYPE_MANGA -> binding.hvSortingSelector.selectByValue(sortingList[0])
                    SearchParams.SEARCH_EPISODES -> binding.hvSortingSelector.selectByValue(sortingList[1])
                    SearchParams.SEARCH_SCORE -> binding.hvSortingSelector.selectByValue(sortingList[2])
                }
            }
        }

        if (isRunAsChild && listener != null) {
            search()
        }
    }

    private fun search() {

        val query = binding.etQuery.text.toString().trim { it <= ' ' }

        if (query.length < 3 && query.isNotEmpty()) {
            binding.tilQuery.error = getString(R.string.search__too_small_query)
            return
        }

        searchParams.query = query
        searchParams.startDate = startDate
        searchParams.finishDate = finishDate

        listener?.invoke(searchParams)
    }

    private fun editStartDate() {

        var date: Date? = null
        try {
            date = DateHelper.MAL_FORMAT.parse(startDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        val millis = if (DateHelper.UNKNOWN_DATE == startDate) 0 else date?.time ?: 0
        ClearButtonDatePickerDialog.show(activity, millis, startDateSetListener)
    }

    private fun editFinishDate() {

        var date: Date? = null
        try {
            date = DateHelper.MAL_FORMAT.parse(finishDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        val millis = if (DateHelper.UNKNOWN_DATE == finishDate) 0 else date?.time ?: 0
        ClearButtonDatePickerDialog.show(activity, millis, finishDateSetListener)
    }

    fun setParams(params: SearchParams?, type: TitleType) {

        isRunAsChild = params != null

        if (params != null) {
            searchParams = params
        }

        this.type = type
        setupFilters()
    }*/
}
