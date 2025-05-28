/*
 *abiola 2022
 */

package com.mshdabiola.search

import androidx.lifecycle.ViewModel
import com.mshdabiola.data.repository.IMediaRepository

class SearchViewModel(
    private val mediaRepository: IMediaRepository,
) : ViewModel()
