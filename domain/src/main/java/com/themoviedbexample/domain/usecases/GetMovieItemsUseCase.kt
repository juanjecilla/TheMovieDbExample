package com.themoviedbexample.domain.usecases

import com.themoviedbexample.domain.common.BaseFlowableUseCase
import com.themoviedbexample.domain.common.FlowableRxTransformer
import com.themoviedbexample.domain.entities.MovieSourcesEntity
import com.themoviedbexample.domain.repositories.MovieRepository
import io.reactivex.Flowable

class GetMovieItemsUseCase(
    private val transformer: FlowableRxTransformer<MovieSourcesEntity>,
    private val repositories: MovieRepository
) : BaseFlowableUseCase<MovieSourcesEntity>(transformer) {

    override fun createFlowable(data: Map<String, Any>?): Flowable<MovieSourcesEntity> {
        return repositories.getMovieItems()
    }

    fun getMovieItems(): Flowable<MovieSourcesEntity> {
        val data = HashMap<String, String>()
        return single(data)
    }
}