package com.example.imagefilter

import android.graphics.Bitmap
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.imagefilter.Adapter.ThumbnailAdapter
import com.example.imagefilter.Interface.FilterListFragmentListener
import com.example.imagefilter.MainActivity.Main.IMAGE_NAME
import com.example.imagefilter.Utils.BitmapUtils
import com.example.imagefilter.Utils.SpaceItemDecoration
import com.example.imagefilter.databinding.FragmentEditImageBinding
import com.example.imagefilter.databinding.FragmentFilterListBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.zomato.photofilters.FilterPack
import com.zomato.photofilters.imageprocessors.Filter
import com.zomato.photofilters.utils.ThumbnailItem
import com.zomato.photofilters.utils.ThumbnailsManager
import java.sql.Array

/**
 * A simple [Fragment] subclass.
 * Use the [FilterListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FilterListFragment : BottomSheetDialogFragment(), FilterListFragmentListener {

    private var _binding: FragmentFilterListBinding? = null
    private val binding get() = _binding!!

    internal var listener: FilterListFragmentListener?=null
    internal lateinit var adapter:ThumbnailAdapter
    internal lateinit var thumbnailItemList: MutableList<ThumbnailItem>

    companion object{
        internal var instance:FilterListFragment?=null
        internal var bitmap:Bitmap?=null

        fun getInstance(bitmapSave: Bitmap?):FilterListFragment{
            if (instance == null)
            {
                instance = FilterListFragment()
            }
            return instance!!
        }

    }

    fun setListener(listFragmentListener: FilterListFragmentListener)
    {
        this.listener = listFragmentListener
    }

    override fun onFilterSelected(filter: Filter) {
        if (listener != null)
            listener!!.onFilterSelected(filter)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFilterListBinding.inflate(inflater, container, false)
        val view = binding.root
        thumbnailItemList  = ArrayList()
        adapter = ThumbnailAdapter(requireActivity(),thumbnailItemList,this)

        binding.recyclerView.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
        binding.recyclerView.itemAnimator = DefaultItemAnimator()
        val space = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,8f,resources.displayMetrics)
                .toInt()
        binding.recyclerView.addItemDecoration(SpaceItemDecoration(space))
        binding.recyclerView.adapter = adapter

        displayImage(bitmap)

        return view
    }

    fun displayImage(bitmap: Bitmap?) {
        val r = Runnable {
            val thumbImage: Bitmap?

            if (bitmap == null)
                thumbImage = BitmapUtils.getBitmapFromAssets(requireActivity(),MainActivity.Main.IMAGE_NAME,100,100)
            else
                thumbImage = Bitmap.createScaledBitmap(bitmap,100,100,false)

            if (thumbImage == null)
                return@Runnable

            ThumbnailsManager.clearThumbs()
            thumbnailItemList.clear()

            //add notmal bitmap first
            val thumbnailItem = ThumbnailItem()
            thumbnailItem.image = thumbImage
            thumbnailItem.filterName = "Normal"
            ThumbnailsManager.addThumb(thumbnailItem)

            //Add filter pack
            val filters = FilterPack.getFilterPack(requireActivity())

            for (filter in filters)
            {
                val item = ThumbnailItem()
                item.image = thumbImage
                item.filter = filter
                item.filterName = filter.name
                ThumbnailsManager.addThumb(item)
            }

            thumbnailItemList.addAll(ThumbnailsManager.processThumbs(activity))
            requireActivity().runOnUiThread{
                adapter.notifyDataSetChanged()
            }

        }
        Thread(r).start()
    }


}