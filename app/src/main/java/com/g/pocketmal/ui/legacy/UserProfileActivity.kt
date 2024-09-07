package com.g.pocketmal.ui.legacy

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.g.pocketmal.R
import com.g.pocketmal.argument
import com.g.pocketmal.bind
import com.g.pocketmal.data.util.TitleType
import com.g.pocketmal.loadUrl
import com.g.pocketmal.ui.legacy.dialog.YesNoDialog
import com.g.pocketmal.ui.legacy.fragment.ListStatsFragment
import com.g.pocketmal.ui.legacy.presenter.UserProfilePresenter
import com.g.pocketmal.ui.legacy.route.UserProfileRoute
import com.g.pocketmal.ui.legacy.view.UserProfileView
import com.g.pocketmal.ui.legacy.viewentity.UserProfileViewModel
import com.g.pocketmal.ui.poster.PosterActivity
import com.g.pocketmal.ui.utils.CircleTransform
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class UserProfileActivity : SkeletonToolbarActivity(),
    UserProfileView,
    UserProfileRoute {

    private val userId: Int by argument(EXTRA_USER_ID, 0)

    private val userAvatar: ImageView by bind(R.id.iv_avatar)
    private val supporter: TextView by bind(R.id.tv_supporter)
    private val usernameLabel: TextView by bind(R.id.tv_username)
    private val detailsLoading: ProgressBar by bind(R.id.pb_progress)
    private val genderMark: ImageView by bind(R.id.civ_gender)
    private val locationLabel: TextView by bind(R.id.tv_location)
    private val joinedDateLabel: TextView by bind(R.id.tv_joined_date)
    private val birthDateLabel: TextView by bind(R.id.tv_birthday)
    private val root: View by bind(R.id.sv_container)

    private val presenter: UserProfilePresenter by inject { parametersOf(userId, this) }

    private lateinit var animeStatsFragment: ListStatsFragment
    private lateinit var mangaStatsFragment: ListStatsFragment

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && isInMultiWindowMode) {
            R.layout.activity_user_profile_multiwindow
        } else {
            R.layout.activity_user_profile
        }

        setContentView(layout, CHILD_ACTIVITY)

        supportActionBar?.let {
            it.elevation = 0f
        }

        animeStatsFragment = supportFragmentManager
                .findFragmentById(R.id.fragment_anime_stats) as ListStatsFragment
        mangaStatsFragment = supportFragmentManager
                .findFragmentById(R.id.fragment_manga_stats) as ListStatsFragment

        animeStatsFragment.isVisible = false
        mangaStatsFragment.isVisible = false

        presenter.loadUserProfile()
    }

    override fun displayUserInfo(userInfo: UserProfileViewModel) {

        usernameLabel.text = userInfo.name

        bindAvatar(userInfo.avatar)

        if (userInfo.isSupporter) {
            supporter.visibility = View.VISIBLE
        }

        if (userInfo.isNormalGender) {
            genderMark.visibility = View.VISIBLE
            genderMark.setImageResource(userInfo.genderDrawable)
        } else {
            genderMark.visibility = View.GONE
        }

        if (userInfo.isLocationAvailable) {
            locationLabel.visibility = View.VISIBLE
            locationLabel.text = userInfo.location
        } else {
            locationLabel.visibility = View.GONE
        }

        if (userInfo.isBirthdayAvailable) {
            birthDateLabel.visibility = View.VISIBLE
            birthDateLabel.text = userInfo.birthday
        } else {
            birthDateLabel.visibility = View.GONE
        }

        joinedDateLabel.text = getString(R.string.user_profile__join_date, userInfo.joinDate)

        animeStatsFragment.setUserData(userInfo, TitleType.ANIME)
        mangaStatsFragment.setUserData(userInfo, TitleType.MANGA)

        animeStatsFragment.isVisible = true
        //mangaStatsFragment.setVisible(true);
    }

    override fun showProgress() {
        detailsLoading.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        detailsLoading.visibility = View.GONE
    }

    override fun openOnMal(url: String) {
        openLink(url)
    }

    override fun openUserImage(url: String) {
        PosterActivity.start(this, url)
    }

    override fun showFailNotification() {

        Snackbar.make(
            root,
            R.string.user_profile__get_user_details_fail,
            Snackbar.LENGTH_INDEFINITE
        )
                .setAction(R.string.tryAgain) { presenter.loadUserProfile() }
                .show()
    }

    private fun bindAvatar(url: String?) {

        userAvatar.setOnClickListener {
            presenter.imageClick()
        }

        userAvatar.loadUrl(
                url = url,
                placeholder = R.drawable.empty_avatar,
                transformation = CircleTransform()
        )
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.profile_activity_actions, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            /*R.id.action_go_to_site -> {
                presenter.viewOnMalClick()
                true
            }*/
            R.id.action_logout -> {
                YesNoDialog(this, getCurrentTheme(), getString(R.string.logoutConfirmation),
                    yesClick = {
                        presenter.logout()
                    }
                ).show()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {

        private const val EXTRA_USER_ID = "extra.user_id"

        fun start(context: Context, userId: Int) {
            val intent = Intent(context, UserProfileActivity::class.java)
                    .putExtra(EXTRA_USER_ID, userId)
            context.startActivity(intent)
        }
    }
}