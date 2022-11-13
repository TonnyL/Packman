package com.lizhaotailang.packman.android

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.cachedIn
import com.lizhaotailang.packman.android.jobs.JobsDataSource
import com.lizhaotailang.packman.common.data.Pipeline
import com.lizhaotailang.packman.common.network.ApolloGraphQLClient
import com.lizhaotailang.packman.common.network.Resource
import com.lizhaotailang.packman.common.network.Status
import com.lizhaotailang.packman.common.network.api.GitLabApi
import com.lizhaotailang.packman.common.ui.new.Variant
import io.ktor.client.call.body
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val gitLabApi = GitLabApi()

    private val _requestFlow: MutableStateFlow<Resource<Pipeline>?> =
        MutableStateFlow(value = null)
    val requestFlow: StateFlow<Resource<Pipeline>?> = _requestFlow

    private val _snackbarMessage = MutableStateFlow("")
    val snackbarMessage: StateFlow<String> = _snackbarMessage

    val jobsFlow by lazy {
        Pager(
            config = defaultPagingConfig,
            pagingSourceFactory = {
                JobsDataSource(
                    apolloClient = ApolloGraphQLClient(accessToken = "Q4V7iHH2beDvyhKW1YJT").apolloClient
                )
            }
        ).flow.cachedIn(scope = viewModelScope)
    }

    fun showSnackbarMessage(newMessage: String) {
        viewModelScope.launch {
            try {
                _snackbarMessage.emit(newMessage)
            } catch (e: Exception) {
                Log.d("HomeViewModel", null, e)
            }
        }
    }

    fun triggerNewPipeline(
        branch: String,
        variants: List<Variant>
    ) {
        if (_requestFlow.value?.status == Status.LOADING) {
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                _requestFlow.emit(Resource.loading(data = null))

                val pipeline: Pipeline = gitLabApi.triggerPipeline(
                    branch = branch,
                    variants = variants
                ).body()

                _requestFlow.emit(Resource.success(data = pipeline))
            } catch (e: Exception) {
                _requestFlow.emit(Resource.failed(exception = e, data = null))

                showSnackbarMessage("Failed to run pipeline")
            }
        }
    }

    companion object {

        const val KEY_HOME_SCREEN = "key_home_screen"

    }

}
