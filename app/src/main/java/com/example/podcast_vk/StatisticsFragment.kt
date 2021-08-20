package com.example.podcast_vk

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.podcast_vk.entity.reaction.ReactionResponse
import com.jjoe64.graphview.series.BarGraphSeries
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.jjoe64.graphview.series.PointsGraphSeries
import kotlinx.android.synthetic.main.fragment_statistics.*


class StatisticsFragment : Fragment(R.layout.fragment_statistics) {
    private val viewModel: PodcastViewModel by activityViewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        iv_back.setOnClickListener {
            findNavController().popBackStack()
        }
        val response = viewModel.getReactionsFromAssets(view.context)
        if (response != null) {

            lineGraphSeries(response)

            pointslineGraphSeries(response)

            barGraphSeries(response)
        } else {
            graph1.visibility = View.GONE
            graph2.visibility = View.GONE
            graph3.visibility = View.GONE

            empty1.visibility = View.VISIBLE
            empty2.visibility = View.VISIBLE
            empty3.visibility = View.VISIBLE
        }
    }

    private fun barGraphSeries(respon: ReactionResponse) {
        val list = mutableListOf<DataPoint?>()

        val y = mutableListOf<Int?>()
        val x = mutableListOf<Int?>()

        if (respon.episodes != null) {
            if (!respon.episodes[0].statistics.isNullOrEmpty()) {
                respon.episodes[0].statistics?.map { it?.time }?.let { x.addAll(it) }

                x.sortBy { it }

                if (!x.isNullOrEmpty()) {
                    for (i in respon.episodes[0].statistics!!) {
                        if (y.isEmpty() || y.size <= x.indexOf(i?.time))
                            y.add(1)
                        else
                            y[x.indexOf(i?.time)]?.plus(1)
                    }
                }
            }
        }

        for (i in x) {
            for (j in y) {
                list.add(j?.let { i?.toDouble()?.let { it1 -> DataPoint(it1, it.toDouble()) } })
            }
        }

        if (list.isNullOrEmpty()) {
            graph2.visibility = View.GONE
            empty2.visibility = View.VISIBLE
        } else {
            graph2.visibility = View.VISIBLE
            empty2.visibility = View.GONE
            val series: BarGraphSeries<DataPoint> =
                BarGraphSeries((list as List<DataPoint?>).toTypedArray())
            series.spacing = 50
            series.color = R.color.graaay
            graph2.addSeries(series)
        }
    }

    private fun pointslineGraphSeries(respon: ReactionResponse) {
        val list = mutableListOf<DataPoint?>()
        if (respon.episodes != null)
            for (i in respon.episodes) {
                if (i.statistics != null) {
                    val a =
                        i.statistics.map {
                            it?.age?.toDouble()?.let { it1 ->
                                it.reactionId?.toDouble()?.let { it2 ->
                                    DataPoint(
                                        it1,
                                        it2
                                    )
                                }
                            }
                        }
                    list.addAll(a)
                }
            }

        list.sortBy { it?.x }

        if (list.isNullOrEmpty()) {
            graph3.visibility = View.GONE
            empty3.visibility = View.VISIBLE
        } else {
            graph3.visibility = View.VISIBLE
            empty3.visibility = View.GONE
            val series: PointsGraphSeries<DataPoint> =
                PointsGraphSeries((list as List<DataPoint?>).toTypedArray())
            graph3.addSeries(series)
        }
    }


    private fun lineGraphSeries(respon: ReactionResponse) {
        val list = mutableListOf<DataPoint?>()
        if (respon.episodes != null)
            for (i in respon.episodes) {
                if (i.statistics != null) {
                    val a =
                        i.statistics.map {
                            it?.age?.toDouble()?.let { it1 ->
                                it.reactionId?.toDouble()?.let { it2 ->
                                    DataPoint(
                                        it1,
                                        it2
                                    )
                                }
                            }
                        }
                    list.addAll(a)
                }
            }

        list.sortBy { it?.x }

        if (list.isNullOrEmpty()) {
            graph1.visibility = View.GONE
            empty1.visibility = View.VISIBLE
        } else {
            graph1.visibility = View.VISIBLE
            empty1.visibility = View.GONE
            val series: LineGraphSeries<DataPoint> =
                LineGraphSeries((list as List<DataPoint?>).toTypedArray())
            graph1.addSeries(series)
        }

    }
}