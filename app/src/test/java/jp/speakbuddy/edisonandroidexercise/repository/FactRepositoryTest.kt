package jp.speakbuddy.edisonandroidexercise.repository

import app.cash.turbine.test
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import jp.speakbuddy.edisonandroidexercise.FactStore
import jp.speakbuddy.edisonandroidexercise.network.FactNetworkDataSource
import jp.speakbuddy.edisonandroidexercise.network.FactResponse
import jp.speakbuddy.edisonandroidexercise.storage.FactLocalDataSource
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import java.io.IOException

class FactRepositoryTest {

    private fun createFactRepository(
        factNetworkDataSource: FactNetworkDataSource = mockk(),
        factLocalDataSource: FactLocalDataSource = mockk(),
    ): FactRepository {
        return FactRepository(factNetworkDataSource, factLocalDataSource)
    }

    @Test
    fun リモートAPIからFactを取得する() = runTest {
        val mockFactNetworkDataSource = mockk<FactNetworkDataSource>() {
            every { fetchFact() } returns flowOf(FactResponse("Fact", 4))
        }
        val mockFactLocalDataSource = mockk<FactLocalDataSource>() {
            coEvery { saveFact(any()) } just runs
        }
        val repository = createFactRepository(factNetworkDataSource = mockFactNetworkDataSource, factLocalDataSource = mockFactLocalDataSource)

        repository.fetchFact().test {
            val result = awaitItem()
            result.fact shouldBe "Fact"
            result.length shouldBe 4

            coVerify(exactly = 1) {
                mockFactLocalDataSource.saveFact(FactResponse("Fact", 4))
            }

            coVerify(exactly = 0) {
                mockFactLocalDataSource.factList
            }

            awaitComplete()
        }
    }

    @Test
    fun リモートAPIからFactの取得に失敗した場合はローカルから取得する() = runTest {
        val mockFactNetworkDataSource = mockk<FactNetworkDataSource>() {
            every { fetchFact() } returns flow { throw IOException() }
        }
        val mockFactLocalDataSource = mockk<FactLocalDataSource>() {
            coEvery { saveFact(any()) } just runs
            coEvery { factList } returns flowOf(mockk<FactStore> {
                every { fact } returns "Local Fact"
                every { length } returns 5
            })
        }
        val repository = createFactRepository(factNetworkDataSource = mockFactNetworkDataSource, factLocalDataSource = mockFactLocalDataSource)

        repository.fetchFact().test {
            val result = awaitItem()
            result.fact shouldBe "Local Fact"
            result.length shouldBe 5

            awaitComplete()
        }
    }
}
