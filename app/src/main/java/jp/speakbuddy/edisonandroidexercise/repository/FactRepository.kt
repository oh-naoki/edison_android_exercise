package jp.speakbuddy.edisonandroidexercise.repository

import jp.speakbuddy.edisonandroidexercise.network.FactNetworkDataSource
import jp.speakbuddy.edisonandroidexercise.network.FactResponse
import jp.speakbuddy.edisonandroidexercise.storage.FactLocalDataSource
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

// MEMO: 今回は interface を切らずに mockk を利用することでテスタブルにした。ここはチームの方針でテスト時にFake実装に差し替えるなら interface を切る
class FactRepository @Inject constructor(
    private val factNetworkDataSource: FactNetworkDataSource,
    private val factLocalDataSource: FactLocalDataSource,
) {
    fun fetchFact() = factNetworkDataSource.fetchFact().onEach {
        factLocalDataSource.saveFact(it)
    }.catch { // MEMO: 本来は Room とかで複数件保存しておいてランダムで取得するような仕組みが必要
        val fact = factLocalDataSource.factList.first()
        emit(
            FactResponse(
                fact = fact.fact, length = fact.length
            )
        )
    }
}
