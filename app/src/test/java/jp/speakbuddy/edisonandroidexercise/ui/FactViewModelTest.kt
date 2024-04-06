package jp.speakbuddy.edisonandroidexercise.ui

import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import jp.speakbuddy.edisonandroidexercise.network.FactResponse
import jp.speakbuddy.edisonandroidexercise.repository.FactRepository
import jp.speakbuddy.edisonandroidexercise.ui.fact.FactViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory

class FactViewModelTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createFactViewModel(
        factRepository: FactRepository = mockk(),
    ): FactViewModel {
        return FactViewModel(factRepository)
    }

    @TestFactory
    fun データ取得結果をUIに反映できること(): List<DynamicTest> {
        data class TestCase(
            val response: Flow<FactResponse>,
            val assertion: (FactViewModel) -> Unit,
        )

        val error = RuntimeException()
        val testCases = listOf(
            TestCase(
                response = flowOf(FactResponse(fact = "Fact1", length = 5)),
                assertion = { viewModel ->
                    viewModel.uiState.value.fact shouldBeEqual FactResponse(fact = "Fact1", length = 5)
                    viewModel.uiState.value.error shouldBe null
                },
            ),
            TestCase(
                response = flow { throw error },
                assertion = { viewModel ->
                    viewModel.uiState.value.error shouldBe error
                },
            ),
        )

        return testCases.map { testCase ->
            DynamicTest.dynamicTest(testCase.toString()) { // Arrange
                val mockFactRepository = mockk<FactRepository>() {
                    every { fetchFact() } returns testCase.response
                }
                val viewModel = createFactViewModel(factRepository = mockFactRepository)

                viewModel.updateFact()

                testCase.assertion(viewModel)
            }
        }
    }

    @TestFactory
    fun データ取得結果に応じたUI状態を取得できること(): List<DynamicTest> {
        data class TestCase(
            val response: FactResponse,
            val assertion: (FactViewModel) -> Unit,
        )

        val testCases = listOf(
            TestCase(
                response = FactResponse(fact = "Fact1", length = 5),
                assertion = { viewModel ->
                    viewModel.uiState.value.isLongFact shouldBe false
                    viewModel.uiState.value.containsCats shouldBe false
                },
            ),
            TestCase(
                response = FactResponse(fact = "Fact1", length = 100),
                assertion = { viewModel ->
                    viewModel.uiState.value.isLongFact shouldBe true
                    viewModel.uiState.value.containsCats shouldBe false
                },
            ),
            TestCase(
                response = FactResponse(fact = "Fact1 with cats", length = 5),
                assertion = { viewModel ->
                    viewModel.uiState.value.isLongFact shouldBe false
                    viewModel.uiState.value.containsCats shouldBe true
                },
            ),
            TestCase(
                response = FactResponse(fact = "Fact1 with Cats", length = 5),
                assertion = { viewModel ->
                    viewModel.uiState.value.isLongFact shouldBe false
                    viewModel.uiState.value.containsCats shouldBe true
                },
            ),
        )

        return testCases.map { testCase ->
            DynamicTest.dynamicTest(testCase.toString()) { // Arrange
                val mockFactRepository = mockk<FactRepository>() {
                    every { fetchFact() } returns flowOf(testCase.response)
                }
                val viewModel = createFactViewModel(factRepository = mockFactRepository)

                viewModel.updateFact()

                testCase.assertion(viewModel)
            }
        }
    }
}
