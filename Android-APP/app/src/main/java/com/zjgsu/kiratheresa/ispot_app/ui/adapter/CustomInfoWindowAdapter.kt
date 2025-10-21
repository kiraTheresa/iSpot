package com.zjgsu.kiratheresa.iblog.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.amap.api.maps.AMap
import com.amap.api.maps.model.Marker
import com.zjgsu.kiratheresa.iblog.R
import com.zjgsu.kiratheresa.iblog.model.MarkerInfo
import com.zjgsu.kiratheresa.iblog.model.MarkerType

class CustomInfoWindowAdapter(
    private val context: Context,
    private val aMap: AMap,
    private val onNavigateClick: (MarkerInfo) -> Unit,
    private val onDetailClick: (MarkerInfo) -> Unit
) : AMap.InfoWindowAdapter {

    override fun getInfoWindow(marker: Marker): View {
        return createView(marker)
    }

    override fun getInfoContents(marker: Marker): View? {
        return null // 返回null表示使用getInfoWindow // error
    }

    private fun createView(marker: Marker): View {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_custom_info_window, null)
        val info = getMarkerInfoFromMarker(marker) ?: return view  // 无数据就返回空布局

        bindData(view, info)
        setupClickListeners(view, info, marker)
        return view
    }


    private fun getMarkerInfoFromMarker(marker: Marker): MarkerInfo? {
        val pos = marker.position ?: return null   // ← 关键安全检查

        return MarkerInfo(
            id = marker.id,
            userId = "user_${marker.id}",
            title = marker.title ?: "未知位置",
            snippet = marker.snippet,
            lat = pos.latitude,
            lng = pos.longitude,
            type = MarkerType.POI
        )
    }


    private fun bindData(view: View, markerInfo: MarkerInfo) {
        val tvTitle = view.findViewById<TextView>(R.id.tvTitle)
        val tvSnippet = view.findViewById<TextView>(R.id.tvSnippet)
        val layoutUserInfo = view.findViewById<LinearLayout>(R.id.layoutUserInfo)
        val tvUsername = view.findViewById<TextView>(R.id.tvUsername)
        val btnDetail = view.findViewById<Button>(R.id.btnDetail)

        tvTitle.text = markerInfo.title
        tvSnippet.text = markerInfo.snippet ?: "暂无描述"

        // 根据标记点类型显示不同的用户信息
        when (markerInfo.type) {
            MarkerType.USER, MarkerType.FRIEND -> {
                layoutUserInfo.visibility = View.VISIBLE
                // 这里可以设置用户信息
                tvUsername.text = "用户信息"
            }
            else -> {
                layoutUserInfo.visibility = View.GONE
            }
        }

        // 根据类型设置不同的按钮文本
        when (markerInfo.type) {
            MarkerType.USER, MarkerType.FRIEND -> {
                btnDetail.text = "查看资料"
            }
            MarkerType.CHECK_IN -> {
                btnDetail.text = "查看动态"
            }
            MarkerType.COLLECTION -> {
                btnDetail.text = "查看详情"
            }
            else -> {
                btnDetail.text = "详情"
            }
        }
    }

    private fun setupClickListeners(view: View, markerInfo: MarkerInfo, marker: Marker) {
        val btnNavigate = view.findViewById<Button>(R.id.btnNavigate)
        val btnDetail = view.findViewById<Button>(R.id.btnDetail)

        btnNavigate.setOnClickListener {
            onNavigateClick(markerInfo)
            marker.hideInfoWindow() // 修正：在 marker 上调用
        }

        btnDetail.setOnClickListener {
            onDetailClick(markerInfo)
            marker.hideInfoWindow() // 修正：在 marker 上调用
        }
    }
}