package com.mlefrapper.clean.ui.navigationbar

import com.mlefrapper.clean.ui.base.BaseViewModel
import com.mlefrapper.clean.util.singleSharedFlow
import com.mlefrapper.data.util.DispatchersProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@HiltViewModel
class NavigationBarSharedViewModel @Inject constructor(
    dispatchers: DispatchersProvider
) : BaseViewModel(dispatchers) {

    private val _bottomItem = singleSharedFlow<BottomNavigationBarItem>()
    val bottomItem = _bottomItem.asSharedFlow()

    fun onBottomItemClicked(bottomItem: BottomNavigationBarItem) = launchOnMainImmediate {
        _bottomItem.emit(bottomItem)
    }
}