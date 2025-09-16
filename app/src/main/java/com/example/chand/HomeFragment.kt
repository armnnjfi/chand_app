
package com.example.chand

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.chand.DataBase.ChandDatabase
import com.example.chand.ViewModel.watchlist.WatchlistRepository
import com.example.chand.ViewModel.watchlist.WatchlistViewModel
import com.example.chand.ViewModel.watchlist.WatchlistViewModelFactory
import com.example.chand.adapters.WatchlistAdapter
import com.example.chand.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    // ViewModel
    private val viewModel: WatchlistViewModel by activityViewModels {
        WatchlistViewModelFactory(
            WatchlistRepository(ChandDatabase.getDatabase(requireContext()).dao())
        )
    }

    // Adapter
    private val watchlistAdapter by lazy {
        WatchlistAdapter { item ->
            viewModel.removeFromWatchlist(item)
        }
    }

    private val handler = Handler(Looper.getMainLooper())
    private val updateRunnable = object : Runnable {
        override fun run() {
            viewModel.updateWatchlistPrices()
            handler.postDelayed(this, 1 * 60 * 1000) // هر 1 دقیقه
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // تنظیم RecyclerView
        binding.apply {
            recyclerView.setLayoutManager(GridLayoutManager(context, 2))
            recyclerView.adapter = watchlistAdapter

            addNewCurrencyBtn.setOnClickListener {
                val bottomSheet = BottomSheetCurrencyListFragment()
                bottomSheet.show(childFragmentManager, "BottomSheetCurrencyList")
            }
        }

        // مشاهده تغییرات LiveData
        viewModel.allItems.observe(viewLifecycleOwner) { items ->
            watchlistAdapter.differ.submitList(items)
        }

        // شروع آپدیت دوره‌ای
        handler.post(updateRunnable)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(updateRunnable) // متوقف کردن آپدیت‌ها
    }

    override fun onResume() {
        super.onResume()
        handler.post(updateRunnable) // از سر گرفتن آپدیت‌ها
    }
}