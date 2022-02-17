package org.cnodejs.android.md.ui.widget

import android.view.LayoutInflater
import android.view.View
import com.takwolf.android.hfrecyclerview.HeaderAndFooterRecyclerView
import org.cnodejs.android.md.R
import org.cnodejs.android.md.databinding.FooterLoadMoreBinding

class LoadMoreFooter(private val binding: FooterLoadMoreBinding) : com.takwolf.android.hfrecyclerview.loadmorefooter.LoadMoreFooter(binding.root) {
    companion object {
        fun create(layoutInflater: LayoutInflater, recyclerView: HeaderAndFooterRecyclerView): LoadMoreFooter {
            val binding = FooterLoadMoreBinding.inflate(layoutInflater, recyclerView.footerViewContainer, false)
            return LoadMoreFooter(binding)
        }
    }

    init {
        binding.tvText.setOnClickListener {
            checkDoLoadMore()
        }
        preloadOffset = 1
    }

    override fun onUpdateViews(footerView: View, @State state: Int) {
        when (state) {
            STATE_DISABLED -> {
                binding.loadingBar.visibility = View.INVISIBLE
                binding.tvText.visibility = View.INVISIBLE
                binding.tvText.text = null
                binding.tvText.isClickable = false
            }
            STATE_LOADING -> {
                binding.loadingBar.visibility = View.VISIBLE
                binding.tvText.visibility = View.INVISIBLE
                binding.tvText.text = null
                binding.tvText.isClickable = false
            }
            STATE_FINISHED -> {
                binding.loadingBar.visibility = View.INVISIBLE
                binding.tvText.visibility = View.VISIBLE
                binding.tvText.setText(R.string.load_more_finished)
                binding.tvText.isClickable = false
            }
            STATE_ENDLESS -> {
                binding.loadingBar.visibility = View.INVISIBLE
                binding.tvText.visibility = View.VISIBLE
                binding.tvText.text = null
                binding.tvText.isClickable = true
            }
            STATE_FAILED -> {
                binding.loadingBar.visibility = View.INVISIBLE
                binding.tvText.visibility = View.VISIBLE
                binding.tvText.setText(R.string.load_more_failed)
                binding.tvText.isClickable = true
            }
        }
    }
}
