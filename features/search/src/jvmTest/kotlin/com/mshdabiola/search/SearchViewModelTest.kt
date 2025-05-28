/*
 *abiola 2022
 */

package com.mshdabiola.search

import com.mshdabiola.data.repository.IMediaRepository
import com.mshdabiola.testing.fake.testDataModule
import com.mshdabiola.testing.util.MainDispatcherRule
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject
import kotlin.test.Test

class SearchViewModelTest : KoinTest {
    @get:Rule(order = 1)
    val tmpFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

    @get:Rule(order = 2)
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule(order = 3)
    val koinTestRule =
        KoinTestRule.create {
            this.modules(testDataModule)
        }
    private val mediaRepository by inject<IMediaRepository>()

    @Test
    fun init() =
        runTest(mainDispatcherRule.testDispatcher) {
            val viewModel =
                SearchViewModel(
                    mediaRepository = mediaRepository,
                )

//            viewModel
//                .notes
//                .test {
//                    var state = awaitItem()
//
//                    assertTrue(state is Result.Loading)
//
//                    state = awaitItem()
//
//                    assertTrue(state is Result.Success)
//
//                    assertEquals(
//                        10,
//                        state.data.size,
//                    )
//
//                    cancelAndIgnoreRemainingEvents()
//                }
        }
}
