package com.zjgsu.kiratheresa.iblog.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.amap.api.maps.AMap
import com.amap.api.maps.model.Marker
import com.zjgsu.kiratheresa.iblog.R
import com.zjgsu.kiratheresa.iblog.databinding.LayoutCustomInfoWindowBinding
import com.zjgsu.kiratheresa.iblog.model.MarkerInfo
import com.zjgsu.kiratheresa.iblog.model.MarkerType

class CustomInfoWindowAdapter(
    private val context: Context,
    private val aMap: AMap,
    private val onNavigateClick: (MarkerInfo) -> Unit,
    private val onDetailClick: (MarkerInfo) -> Unit
) : AMap.InfoWindowAdapter {

    private var binding: LayoutCustomInfoWindowBinding? = null

    override fun getInfoWindow(marker: Marker): View {
        return createView(marker)
    }

    override fun getInfoContents(marker: Marker): View? {
        return null // 返回null表示使用getInfoWindow
    }

    private fun createView(marker: Marker): View {
        val binding = LayoutCustomInfoWindowBinding.inflate(
            LayoutInflater.from(context)
        )
        this.binding = binding

        // 获取标记点信息（这里需要从标记点中提取或通过其他方式获取）
        val markerInfo = getMarkerInfoFromMarker(marker)

        markerInfo?.let { info ->
            bindData(binding, info)
            setupClickListeners(binding, info)
        }

        return binding.root
    }

    private fun getMarkerInfoFromMarker(marker: Marker): MarkerInfo? {
        // 这里应该从你的数据源中根据标记点获取对应的MarkerInfo
        // 暂时返回一个模拟数据
        return MarkerInfo(
            id = marker.id,
            userId = "user_${marker.id}",
            title = marker.title ?: "未知位置",
            snippet = marker.snippet,
            lat = marker.position.latitude,
            lng = marker.position.longitude,
            type = MarkerType.POI
        )
    }

    private fun bindData(binding: LayoutCustomInfoWindowBinding, markerInfo: MarkerInfo) {
        binding.tvTitle.text = markerInfo.title
        binding.tvSnippet.text = markerInfo.snippet ?: "暂无描述"

        // 根据标记点类型显示不同的用户信息
        when (markerInfo.type) {
            MarkerType.USER, MarkerType.FRIEND -> {
                binding.layoutUserInfo.visibility = View.VISIBLE
                markerInfo.user?.let { user ->
                    binding.tvUsername.text = user.nickname ?: user.username
                    // 这里可以加载用户头像
                }
            }
            else -> {
                binding.layoutUserInfo.visibility = View.GONE
            }
        }

        // 根据类型设置不同的按钮文本
        when (markerInfo.type) {
            MarkerType.USER, MarkerType.FRIEND -> {
                binding.btnDetail.text = "查看资料"
            }
            MarkerType.CHECK_IN -> {
                binding.btnDetail.text = "查看动态"
            }
            MarkerType.COLLECTION -> {
                binding.btnDetail.text = "查看详情"
            }
            else -> {
                binding.btnDetail.text = "详情"
            }
        }
    }

    private fun setupClickListeners(binding: LayoutCustomInfoWindowBinding, markerInfo: MarkerInfo) {
        binding.btnNavigate.setOnClickListener {
            onNavigateClick(markerInfo)
            // 关闭信息窗口
            aMap.hideInfoWindow()
        }

        binding.btnDetail.setOnClickListener {
            onDetailClick(markerInfo)
            // 关闭信息窗口
            aMap.hideInfoWindow()
        }

        // 整个信息窗口的点击事件
        binding.root.setOnClickListener {
            // 防止点击信息窗口时触发地图点击事件
        }
    }

    fun destroy() {
        binding = null
    }
}