package com.chesire.malime.util

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import com.chesire.malime.core.flags.SupportedService
import com.chesire.malime.util.sec.Decryptor
import com.chesire.malime.util.sec.Encryptor
import com.chesire.malime.view.preferences.SortOption

private const val authAlias: String = "private_auth"
private const val refreshAlias: String = "private_refresh"
private const val preferenceAuth: String = "auth"
private const val preferenceRefresh: String = "refresh"
private const val preferenceUserId: String = "userId"
private const val preferenceUsername: String = "username"
private const val preferencePrimaryService: String = "primaryService"
private const val preferenceAllowCrashReporting: String = "allowCrashReporting"
private const val preferenceFilterLength: String = "animeFilterLength"
private const val preferenceSeriesUpdateSchedulerEnabled: String = "seriesUpdateSchedulerEnabled"
const val preferenceFilter: String = "filter"
const val preferenceSort: String = "sort"

class SharedPref(
    context: Context
) {
    val sharedPrefFile: String = "malime_shared_pref"

    private val sharedPreferences =
        context.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
    private val encryptor = Encryptor(context.applicationContext)
    private val decryptor = Decryptor()

    fun getAuth(): String {
        val text = sharedPreferences.getString(preferenceAuth, "")

        return if (text.isNotBlank()) {
            decryptor.decryptData(
                authAlias,
                Base64.decode(text, Base64.DEFAULT)
            )
        } else {
            ""
        }
    }

    fun putAuth(auth: String): SharedPref {
        val encrypted = encryptor.encryptText(authAlias, auth)

        sharedPreferences.edit()
            .putString(preferenceAuth, Base64.encodeToString(encrypted, Base64.DEFAULT))
            .apply()

        return this
    }

    fun getRefresh(): String {
        val text = sharedPreferences.getString(preferenceRefresh, "")

        return if (text.isNotBlank()) {
            decryptor.decryptData(
                refreshAlias,
                Base64.decode(text, Base64.DEFAULT)
            )
        } else {
            ""
        }
    }

    fun putRefresh(refresh: String): SharedPref {
        val encrypted = encryptor.encryptText(refreshAlias, refresh)

        sharedPreferences.edit()
            .putString(preferenceRefresh, Base64.encodeToString(encrypted, Base64.DEFAULT))
            .apply()

        return this
    }

    fun getUserId(): Int {
        return sharedPreferences.getInt(preferenceUserId, 0)
    }

    fun putUserId(userId: Int): SharedPref {
        sharedPreferences.edit()
            .putInt(preferenceUserId, userId)
            .apply()

        return this
    }

    fun getUsername(): String {
        return sharedPreferences.getString(preferenceUsername, "")
    }

    fun putUsername(username: String): SharedPref {
        sharedPreferences.edit()
            .putString(preferenceUsername, username)
            .apply()

        return this
    }

    fun getPrimaryService(): SupportedService {
        return SupportedService.valueOf(sharedPreferences.getString(preferencePrimaryService, ""))
    }

    fun putPrimaryService(service: SupportedService): SharedPref {
        sharedPreferences.edit()
            .putString(preferencePrimaryService, service.name)
            .apply()

        return this
    }

    fun getAllowCrashReporting(): Boolean {
        return sharedPreferences.getBoolean(preferenceAllowCrashReporting, true)
    }

    fun getFilter(): BooleanArray {
        if (!hasStoredFilter()) {
            val defaultFilter = getDefaultFilter()
            setFilter(defaultFilter)
            return defaultFilter
        }

        val filterLength = sharedPreferences.getInt(preferenceFilterLength, 0)
        val returnArray = BooleanArray(filterLength)
        for (i in 0 until filterLength) {
            returnArray[i] = sharedPreferences.getBoolean(preferenceFilter + i, false)
        }

        return returnArray
    }

    fun setFilter(input: BooleanArray): SharedPref {
        val editor = sharedPreferences.edit()
        editor.putInt(preferenceFilterLength, input.count())
        for (i in input.indices) {
            editor.putBoolean(preferenceFilter + i, input[i])
        }
        editor.apply()

        return this
    }

    fun getSortOption(): SortOption {
        return SortOption.getOptionFor(
            sharedPreferences.getInt(
                preferenceSort,
                SortOption.Title.id
            )
        )
    }

    fun setSortOption(sortOption: SortOption): SharedPref {
        sharedPreferences.edit()
            .putInt(preferenceSort, sortOption.id)
            .apply()

        return this
    }

    fun getSeriesUpdateSchedulerEnabled(): Boolean {
        return sharedPreferences.getBoolean(preferenceSeriesUpdateSchedulerEnabled, false)
    }

    fun setSeriesUpdateSchedulerEnabled(state: Boolean): SharedPref {
        sharedPreferences.edit()
            .putBoolean(preferenceSeriesUpdateSchedulerEnabled, state)
            .apply()

        return this
    }

    fun clearLoginDetails() {
        sharedPreferences.edit()
            .remove(preferenceAuth)
            .remove(preferenceUsername)
            .remove(preferenceUserId)
            .remove(preferencePrimaryService)
            .apply()
    }

    fun registerOnChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
    }

    fun unregisterOnChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
    }

    private fun hasStoredFilter(): Boolean {
        if (!sharedPreferences.contains(preferenceFilterLength)) {
            return false
        }

        val filterLength = sharedPreferences.getInt(preferenceFilterLength, 0)
        for (i in 0 until filterLength) {
            if (!sharedPreferences.contains(preferenceFilter + i)) {
                return false
            }
        }

        return true
    }

    private fun getDefaultFilter(): BooleanArray {
        return booleanArrayOf(true, false, false, false, false)
    }
}