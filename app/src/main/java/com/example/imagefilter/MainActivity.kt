package com.example.imagefilter

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Typeface
import android.graphics.YuvImage
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import android.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.viewpager.widget.ViewPager
import com.example.imagefilter.Adapter.ViewPagerAdapter
import com.example.imagefilter.Interface.*
import com.example.imagefilter.Utils.BitmapUtils
import com.example.imagefilter.Utils.NonswipeableViewPager
import com.example.imagefilter.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.yalantis.ucrop.UCrop
import com.zomato.photofilters.imageprocessors.Filter
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubFilter
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubFilter
import com.zomato.photofilters.imageprocessors.subfilters.SaturationSubfilter
import ja.burhanrashid52.photoeditor.OnSaveBitmap
import ja.burhanrashid52.photoeditor.PhotoEditor
import ja.burhanrashid52.photoeditor.PhotoEditorView
import java.io.File
import java.lang.Exception
import java.lang.StringBuilder
import java.util.*

class MainActivity : AppCompatActivity(), FilterListFragmentListener, EditImageFragmentListener, BrushFragmentListener,
    EmojiFragmentListener, AddTextFragmentListener, AddFrameFragmentListener {

    val SELECT_GALLERY_PERMISSION = 1000
    val PICK_IMAGE_GALLERY_PERMISSION = 1001

    lateinit var photoEditor: PhotoEditor

    init{
        System.loadLibrary("NativeImageProcessor")
    }

    object Main {
        val IMAGE_NAME = "flash.jpg"
    }

    internal var originalImage:Bitmap?= null
    internal lateinit var filteredImage: Bitmap
    internal lateinit var finalImage: Bitmap

    internal lateinit var filterListFragment: FilterListFragment
    internal lateinit var editImageFragment: EditImageFragment
    internal lateinit var brushFragment: BrushFragment
    internal lateinit var emojiFragment: EmojiFragment
    internal lateinit var addTextFragment: AddTextFragment
    internal lateinit var frameFragment: FrameFragment

    internal var brightnessFinal = 0
    internal var saturationFinal = 1.0f
    internal var contrastFinal = 1.0f

    internal var image_selected_uri:Uri?=null

    internal var imageUri:Uri? = null
    internal val CAMERA_REQUEST:Int = 9999

    private lateinit var binding: ActivityMainBinding
    lateinit var image_preview: PhotoEditorView

    lateinit var btn_filters: CardView
    lateinit var btn_edit:CardView
    lateinit var btn_brush:CardView
    lateinit var btn_emoji:CardView
    lateinit var btn_text:CardView
    lateinit var btn_image:CardView
    lateinit var btn_frame:CardView
    lateinit var btn_crop:CardView




    override fun onBrightnessChanged (brightness: Int) {
        brightnessFinal = brightness
        val myFilter = Filter()
        myFilter.addSubFilter(BrightnessSubFilter(brightness))
        image_preview.source.setImageBitmap(myFilter.processFilter(finalImage.copy(Bitmap.Config.ARGB_8888,true)))
    }

    override fun onSaturationChanged(saturation: Float) {
        saturationFinal = saturation
        val myFilter = Filter()
        myFilter.addSubFilter(SaturationSubfilter(saturation))
        image_preview.source.setImageBitmap(myFilter.processFilter(finalImage.copy(Bitmap.Config.ARGB_8888,true)))
    }

    override fun onConstrantChanged(constrant: Float) {
        contrastFinal = constrant
        val myFilter = Filter()
        myFilter.addSubFilter(ContrastSubFilter(constrant))
        image_preview.source.setImageBitmap(myFilter.processFilter(finalImage.copy(Bitmap.Config.ARGB_8888,true)))
    }

    override fun onEditStarted() {

    }

    override fun onEditCompleted() {
        val bitmap = filteredImage.copy(Bitmap.Config.ARGB_8888,true)
        val myFilter = Filter()
        myFilter.addSubFilter(ContrastSubFilter(contrastFinal))
        myFilter.addSubFilter(SaturationSubfilter(saturationFinal))
        myFilter.addSubFilter(BrightnessSubFilter(brightnessFinal))
        finalImage = myFilter.processFilter(bitmap)

    }

    override fun onFilterSelected(filter: Filter) {
       // resetControls()
        filteredImage = originalImage!!.copy(Bitmap.Config.ARGB_8888,true)
        image_preview.source.setImageBitmap(filter.processFilter(filteredImage))
        finalImage = filteredImage.copy(Bitmap.Config.ARGB_8888,true)
    }

    private fun resetControls() {

        if (editImageFragment!=null)
            editImageFragment.resetControls()
        brightnessFinal = 0
        saturationFinal = 1.0f
        contrastFinal= 1.0f

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //set toolbar
        setSupportActionBar(binding.toolBar.findViewById(R.id.toolBar))
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title="Image Filter"

        image_preview = findViewById(R.id.image_preview)
        btn_filters = findViewById(R.id.btn_filters)
        btn_edit = findViewById(R.id.btn_edit)
        btn_brush=findViewById(R.id.btn_brush)
        btn_emoji=findViewById(R.id.btn_emoji)
        btn_text = findViewById(R.id.btn_add_text)
        btn_image = findViewById(R.id.btn_add_image)
        btn_frame = findViewById(R.id.btn_add_frame)
        btn_crop = findViewById(R.id.btn_crop)

        photoEditor = PhotoEditor.Builder(this@MainActivity,image_preview)
                .setPinchTextScalable(true)
                .setDefaultEmojiTypeface(Typeface.createFromAsset(assets,"emojione-android.ttf"))
                .build()

        loadImage()

        filterListFragment = FilterListFragment.getInstance(null)
        editImageFragment = EditImageFragment.getInstance()
        brushFragment = BrushFragment.getInstance()
        emojiFragment = EmojiFragment.getInstance()
        addTextFragment = AddTextFragment.getInstance()
        frameFragment = FrameFragment.getInstance()

        btn_filters.setOnClickListener {

            if (filterListFragment != null) {
                filterListFragment.show(supportFragmentManager, filterListFragment.tag)
            }
            else
            {
                filterListFragment.setListener(this@MainActivity)
                filterListFragment.show(supportFragmentManager, filterListFragment.tag)

            }
        }

        btn_edit.setOnClickListener{

            if (editImageFragment!=null) {
                editImageFragment.setListener(this@MainActivity)
                editImageFragment.show(supportFragmentManager, editImageFragment.tag)

            }
        }

        btn_brush.setOnClickListener {

            if (brushFragment!=null) {

                photoEditor.setBrushDrawingMode(true)

                brushFragment.setListener(this@MainActivity)
                brushFragment.show(supportFragmentManager, brushFragment.tag)

            }
        }

        btn_emoji.setOnClickListener {
            if (emojiFragment != null)
            {
                emojiFragment.setListener(this@MainActivity)
                emojiFragment.show(supportFragmentManager,emojiFragment.tag)
            }
        }

        btn_text.setOnClickListener{
            if (addTextFragment != null)
            {
                addTextFragment.setListener(this@MainActivity)
                addTextFragment.show(supportFragmentManager,addTextFragment.tag)
            }
        }

        btn_image.setOnClickListener {

            addImageToPicture();
        }


        btn_frame.setOnClickListener{
            if (frameFragment != null)
            {
                frameFragment.setListener(this@MainActivity)
                frameFragment.show(supportFragmentManager,frameFragment.tag)
            }
        }

        btn_crop.setOnClickListener {
            startCrop(image_selected_uri)
        }
    }

    private fun startCrop(uri: Uri?) {
        var destinationFileName = StringBuilder(UUID.randomUUID().toString()).append(".jpg").toString()

        var uCrop = UCrop.of(uri!!,Uri.fromFile(File(cacheDir,destinationFileName)))

        uCrop.start(this@MainActivity)
    }

    private fun addImageToPicture() {
        Dexter.withActivity(this@MainActivity)
                .withPermissions(android.Manifest.permission.READ_EXTERNAL_STORAGE,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(object : MultiplePermissionsListener{
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {

                        if (report!!.areAllPermissionsGranted())
                        {
                            val intent = Intent(Intent.ACTION_PICK)
                            intent.type = "image/*"
                            startActivityForResult(intent,PICK_IMAGE_GALLERY_PERMISSION)
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>?, token: PermissionToken?) {
                        Toast.makeText(this@MainActivity,"Permission denied",Toast.LENGTH_SHORT).show()
                    }

                }).check()

    }

    private fun setupViewPager(viewPager: NonswipeableViewPager?) {
        val  adapter = ViewPagerAdapter(supportFragmentManager)

        //add filter list fragment
        filterListFragment = FilterListFragment()
        filterListFragment.setListener(this)

        //add edit image fragment
        editImageFragment = EditImageFragment()
        editImageFragment.setListener(this)

        adapter.addFragment(filterListFragment,"FILTERS")
        adapter.addFragment(editImageFragment,"EDIT")

        viewPager!!.adapter = adapter
    }

    private fun loadImage() {
        originalImage = BitmapUtils.getBitmapFromAssets(this,Main.IMAGE_NAME,300,300)
        filteredImage = originalImage!!.copy(Bitmap.Config.ARGB_8888,true)
        finalImage = originalImage!!.copy(Bitmap.Config.ARGB_8888,true)
        image_preview.source.setImageBitmap(originalImage)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.action_open)
        {
            openImageFromGallery()
            return true
        }
        else if (id == R.id.action_save)
        {
            saveImageToGallery()
            return true
        }
        else if (id == R.id.action_camera)
        {
            openCamera()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun openCamera() {
        Dexter.withActivity(this)
            .withPermissions(android.Manifest.permission.CAMERA,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object :  MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {

                    if (report!!.areAllPermissionsGranted())
                    {

                        var values = ContentValues();
                        values.put(MediaStore.Images.Media.TITLE,"New Picture")
                        values.put(MediaStore.Images.Media.DESCRIPTION,"Error Camera")
                        imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values)

                        var cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri)
                        startActivityForResult(cameraIntent,CAMERA_REQUEST)
                    }
                    else
                        Toast.makeText(applicationContext,"Permission denied",Toast.LENGTH_SHORT).show()
                }

                override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>?, token: PermissionToken?) {
                    token!!.continuePermissionRequest()
                }

            }).check()
    }

    private fun saveImageToGallery() {

        Dexter.withActivity(this)
                .withPermissions(android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(object :  MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {

                        if (report!!.areAllPermissionsGranted())
                        {


                            photoEditor.saveAsBitmap(object:OnSaveBitmap{
                                override fun onBitmapReady(saveBitmap: Bitmap?) {

                                    val path = BitmapUtils.insertImage(contentResolver,
                                            finalImage,
                                            System.currentTimeMillis().toString()+"_profile.jpg",
                                            "")

                                    if (!TextUtils.isEmpty(path))
                                    {
                                        val snackbar = Snackbar.make(binding.coordinator,"Image saved to gallery",Snackbar.LENGTH_LONG)
                                                .setAction("OPEN",{
                                                    openImage(path)
                                                })
                                        snackbar.show()

                                        //Fix error restore bitmap to default
                                        image_preview.source.setImageBitmap(saveBitmap)
                                    }
                                    else
                                    {
                                        val snackbar = Snackbar.make(binding.coordinator,"Unable to save image",Snackbar.LENGTH_LONG)
                                        snackbar.show()
                                    }
                                }

                                override fun onFailure(e: Exception?) {
                                    val snackbar = Snackbar.make(binding.coordinator, e!!.message.toString(),Snackbar.LENGTH_LONG)
                                    snackbar.show()
                                }

                            })

                        }
                        else
                            Toast.makeText(applicationContext,"Permission denied",Toast.LENGTH_SHORT).show()
                    }

                    override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>?, token: PermissionToken?) {
                        token!!.continuePermissionRequest()
                    }

                }).check()
    }

    private fun openImage(path: String?) {
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.setDataAndType(Uri.parse(path),"image/*")
        startActivity(intent)

    }

    private fun openImageFromGallery() {
         Dexter.withActivity(this)
                .withPermissions(android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(object :  MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {

                        if (report!!.areAllPermissionsGranted())
                        {
                            val intent = Intent(Intent.ACTION_PICK)
                            intent.type = "image/*"
                            startActivityForResult(intent,SELECT_GALLERY_PERMISSION)
                        }
                        else
                            Toast.makeText(applicationContext,"Permission denied",Toast.LENGTH_SHORT).show()
                    }

                    override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>?, token: PermissionToken?) {
                        token!!.continuePermissionRequest()
                    }

                }).check()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK)
        {

            if (requestCode == SELECT_GALLERY_PERMISSION) {
                val bitmap = BitmapUtils.getBitmapFromGallery(this, data!!.data!!, 800, 800)

                image_selected_uri = data.data!! //fix crash

                //clear bitmap memory
                originalImage!!.recycle()
                finalImage!!.recycle()
                filteredImage!!.recycle()

                originalImage = bitmap.copy(Bitmap.Config.ARGB_8888, true)
                filteredImage = originalImage!!.copy(Bitmap.Config.ARGB_8888, true)
                finalImage = originalImage!!.copy(Bitmap.Config.ARGB_8888, true)

                image_preview.source.setImageBitmap(originalImage)
                bitmap.recycle()

                //render selct image thumb
                //filterListFragment.displayImage(bitmap)

                //fix crash
                filterListFragment = FilterListFragment.getInstance(originalImage!!)
                filterListFragment.setListener(this)
            }

            else if (requestCode == CAMERA_REQUEST) {
                val bitmap = BitmapUtils.getBitmapFromGallery(this, imageUri!!, 800, 800)

                image_selected_uri = imageUri!! //fix crash

                //clear bitmap memory
                originalImage!!.recycle()
                finalImage!!.recycle()
                filteredImage!!.recycle()

                originalImage = bitmap.copy(Bitmap.Config.ARGB_8888, true)
                filteredImage = originalImage!!.copy(Bitmap.Config.ARGB_8888, true)
                finalImage = originalImage!!.copy(Bitmap.Config.ARGB_8888, true)

                image_preview.source.setImageBitmap(originalImage)
                bitmap.recycle()

                //render selct image thumb
                //filterListFragment.displayImage(bitmap)

                //fix crash
                filterListFragment = FilterListFragment.getInstance(originalImage!!)
                filterListFragment.setListener(this)
            }
            else if (requestCode == PICK_IMAGE_GALLERY_PERMISSION)
            {
                val bitmap = BitmapUtils.getBitmapFromGallery(this, data!!.data!!,200,200)
                photoEditor.addImage(bitmap)
            }
            else if(requestCode == UCrop.REQUEST_CROP)
                handleCropResult(data)
        }
        else if (resultCode == UCrop.RESULT_ERROR)
            handCropError(data)
    }

    private fun handCropError(data: Intent?) {
        var cropError = UCrop.getError(data!!)
        if (cropError != null)
        {
            Toast.makeText(this@MainActivity,""+cropError.message,Toast.LENGTH_SHORT).show()

        }
        else
        {
            Toast.makeText(this@MainActivity,"Unexpected Error",Toast.LENGTH_SHORT).show()

        }
    }

    private fun handleCropResult(data: Intent?) {
        var resultUri = UCrop.getOutput(data!!)
        if (resultUri != null)
            image_preview.source.setImageURI(resultUri)
        else
            Toast.makeText(this@MainActivity,"Cannot retrieve crop image",Toast.LENGTH_SHORT).show()

    }

    override fun onBrushSizeChangedListener(size: Float) {
        photoEditor.brushSize = (size)
    }

    override fun onBrushOpacityChangedListener(size: Int) {
        photoEditor.setOpacity(size)
    }

    override fun onBrushColorChangedListener(color: Int) {
        photoEditor.brushColor = color
    }

    override fun onBrushStateChangedListener(isEraser: Boolean) {
        if (isEraser)
            photoEditor.brushEraser()
        else
            photoEditor.setBrushDrawingMode(true)
    }

    override fun onEmojiItemSelected(emoji: String) {
        photoEditor.addEmoji(emoji)
    }

    override fun onAddTextListener(typeface: Typeface, text: String, color: Int) {
        photoEditor.addText(typeface,text,color)
    }

    override fun onFrameSelected(frame: Int) {
        val bitmap = BitmapFactory.decodeResource(resources,frame)
        photoEditor.addImage(bitmap)
    }


}