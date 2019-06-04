package com.chesire.malime.flow.oob

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.URLSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chesire.malime.R
import com.chesire.malime.databinding.FragmentAnalyticsBinding
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_analytics.fragmentAnalyticsPrivacy

class AnalyticsFragment : DaggerFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentAnalyticsBinding.inflate(inflater).root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setPrivacySpan()
    }

    private fun setPrivacySpan() {
        val privacyText = getString(R.string.analytics_privacy_end)
        val fullPrivacyString = "${getString(R.string.analytics_privacy_start)} $privacyText"
        val pEnd = fullPrivacyString.indexOf(privacyText)

        fragmentAnalyticsPrivacy.apply {
            movementMethod = LinkMovementMethod.getInstance()
            text = SpannableString(fullPrivacyString)
                .apply {
                    setSpan(
                        URLSpan(getString(R.string.privacy_policy_url)),
                        pEnd,
                        pEnd + privacyText.length,
                        Spanned.SPAN_INCLUSIVE_EXCLUSIVE
                    )
                }
        }
    }
}
