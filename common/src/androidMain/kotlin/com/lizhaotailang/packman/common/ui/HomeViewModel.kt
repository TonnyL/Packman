package com.lizhaotailang.packman.common.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.cachedIn
import com.lizhaotailang.packman.common.data.History
import com.lizhaotailang.packman.common.data.Pipeline
import com.lizhaotailang.packman.common.data.PipelineScheduleListItem
import com.lizhaotailang.packman.common.database.PackmanDatabase
import com.lizhaotailang.packman.common.database.realmFilePath
import com.lizhaotailang.packman.common.defaultPagingConfig
import com.lizhaotailang.packman.common.network.ApolloGraphQLClient
import com.lizhaotailang.packman.common.network.Resource
import com.lizhaotailang.packman.common.ui.jobs.JobsDataSource
import com.lizhaotailang.packman.common.ui.new.Variant
import kotlinx.coroutines.flow.StateFlow

class HomeViewModel(app: Application) : AndroidViewModel(app), HomeViewModelFeatures {

    val database = PackmanDatabase(
        scope = viewModelScope,
        realmFilePath = realmFilePath(getApplication())
    )

    private val delegate = HomeViewModelDelegate(
        scope = viewModelScope,
        database = database
    )

    val jobsFlow by lazy {
        Pager(
            config = defaultPagingConfig,
            pagingSourceFactory = {
                JobsDataSource(
                    apolloClient = ApolloGraphQLClient.CLIENT.apolloClient
                )
            }
        ).flow.cachedIn(scope = viewModelScope)
    }

    init {
        getAllPipelineSchedules(init = true)
    }

    override val requestFlow: StateFlow<Resource<Pipeline>?>
        get() = delegate.requestFlow

    override val allPipelineSchedulesFlow: StateFlow<Resource<List<PipelineScheduleListItem>>>
        get() = delegate.allPipelineSchedulesFlow

    override val snackbarMessage: StateFlow<String>
        get() = delegate.snackbarMessage

    override fun getAllPipelineSchedules(init: Boolean) {
        delegate.getAllPipelineSchedules(init = init)
    }

    override fun showSnackbarMessage(newMessage: String) {
        delegate.showSnackbarMessage(newMessage = newMessage)
    }

    override fun triggerNewPipeline(branch: String, variants: List<Variant>) {
        delegate.triggerNewPipeline(
            branch = branch,
            variants = variants
        )
    }

    override fun deleteHistory(history: History) {
        delegate.deleteHistory(history = history)
    }

    override fun onCleared() {
        super.onCleared()
        database.realm.close()
    }

    companion object {

        const val KEY_HOME_SCREEN = "key_home_screen"

        private const val TAG = "HomeViewModel"

    }

}
