package com.example.lenovo.emptypro.Activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.lenovo.emptypro.ApiCallClasses.RetrofitClasses.GetDataService
import com.example.lenovo.emptypro.ApiCallClasses.RetrofitClasses.RetrofitClientInstance
import com.example.lenovo.emptypro.ModelClasses.AllApiResponse
import com.example.lenovo.emptypro.R
import com.example.lenovo.emptypro.Utils.SharedPrefUtil
import com.glide.slider.library.SliderTypes.TextSliderView
import com.google.android.gms.maps.SupportMapFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.lenovo.emptypro.Listeners.OnFragmentInteractionListener
import com.example.lenovo.emptypro.Utilities.Utilities
import com.example.lenovo.emptypro.Utils.GlobalData
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_advertisement_details.*
import kotlinx.android.synthetic.main.header.*
import java.lang.Exception

class PetDetailsActivity : AppCompatActivity(), View.OnClickListener, OnMapReadyCallback {
    override fun onMapReady(googleMap: GoogleMap?) {

        googleMap!!.uiSettings.isZoomControlsEnabled = false
        googleMap.uiSettings.isMyLocationButtonEnabled = false
        gMap = googleMap
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.img_back -> {
onBackPressed()
            }
            R.id.tv_call -> {
utilities.callDialog(this@PetDetailsActivity,""+petDetailModel!!.userNumber)
            }
            R.id.fl_AdvertisementDetailsFrag -> {

            }
            R.id.tv_bottomChat-> {
                val intent = Intent(this@PetDetailsActivity, P2PChatView::class.java)
                       intent.putExtra("oldPetId", "" + petId)
                startActivity(intent)
            }

        }
    }

    // TODO: Rename and change types of parameters
    private var petId: String? = ""
    var petDetailModel: AllApiResponse.PetDetailRes.PetDetail? = null
    var lati = "0"
    var longi = "0"
    internal var service: GetDataService? = null
    var gMap: GoogleMap? = null
    var TAG = "AdvertisementDetailsFrag "
    internal var utilities = Utilities()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_advertisement_details)
        ll_topHeaderLayout.visibility = View.VISIBLE
        img_back.setOnClickListener(this)
        tv_title.text = "Pet Detail"
        tv_bottomChat.setOnClickListener(this)
        tv_call.setOnClickListener(this)
        fl_AdvertisementDetailsFrag.setOnClickListener(this)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)

        try {
            getOldIntentData()
        } catch (exp: Exception) {

        }
    }

    private fun getOldIntentData() {

        petId = intent.extras!!.getString("oldPetId")
        if (!petId.equals("")) {
            Log.e(TAG + " oldIntent", "petID=" + petId)
            getPetDetail()
        }
    }

    private fun getPetDetail() {
        Log.e(TAG + " getPetDetail", "single-pet/?userID=" + SharedPrefUtil.getUserId(this@PetDetailsActivity) + "&petID=" + petId)
        var call = service!!.getPetDetailsApi(""+SharedPrefUtil.getUserId(this@PetDetailsActivity), "" + petId)

        call.enqueue(object : Callback<AllApiResponse.PetDetailRes> {
            override fun onResponse(call: Call<AllApiResponse.PetDetailRes>, response: Response<AllApiResponse.PetDetailRes>) {
                Log.e("getPetDetail res", "" + Gson().toJson(response.body()))

                if (response.body()!!.status.equals("200") && response.body()!!.data.size > 0) {
                    petDetailModel = response.body()!!.data[0]
                    for (item in petDetailModel!!.images) {
                        val sliderView = TextSliderView(this@PetDetailsActivity)
                        sliderView.image(item.img).setProgressBarVisible(true)
                        slider.addSlider(sliderView)
                    }

                    if(petDetailModel!!.images.size==0)
                    {
                        val sliderView = TextSliderView(this@PetDetailsActivity)
                        sliderView.image(R.drawable.app_logo).setProgressBarVisible(true)
                        slider.addSlider(sliderView)
                    }
                    tv_advertiseLoc.text = "" + petDetailModel!!.userCity + ", " + petDetailModel!!.userState
                    tv_advertiseDate.text = "" + petDetailModel!!.createdOn
                    tv_petTitle.text = petDetailModel!!.petTitle
                    tv_advertiseDesc.text = petDetailModel!!.petDescription
                    tv_advertisePrice.text = petDetailModel!!.petPrice
                    tv_owmerName.text = petDetailModel!!.firstName+" "+petDetailModel!!.lastName
                    tv_adsId.text = ""+petDetailModel!!.petID

                    if(petDetailModel!!.userID.equals(SharedPrefUtil.getUserId(this@PetDetailsActivity)))
{
    ll_bottomChatCall.visibility= View.GONE

}
                    var strDetails=""
                    if(!petDetailModel!!.petTitle.equals(""))
                    {
                        strDetails=strDetails+"Title: "+petDetailModel!!.petTitle+"\n"
                    }
                    if(!petDetailModel!!.category.equals(""))
                    {
                        strDetails=strDetails+"Category: "+petDetailModel!!.category+"\n"
                    }
                    if(!petDetailModel!!.subCategory.equals(""))
                    {
                        strDetails=strDetails+"SubCategory: "+petDetailModel!!.subCategory+"\n"
                    }
                    if(!petDetailModel!!.petAge.equals(""))
                    {
                        strDetails=strDetails+"Pet Age: "+petDetailModel!!.petAge+"\n"
                    }
                    if(!petDetailModel!!.petBreed.equals(""))
                    {
                        strDetails=strDetails+"Pet Breed: "+petDetailModel!!.petBreed+"\n"
                    }
                    if(!petDetailModel!!.petName.equals(""))
                    {
                        strDetails=strDetails+"Pet Name: "+petDetailModel!!.petName+"\n"
                    }
                    if(!petDetailModel!!.petDescription.equals(""))
                    {
                        strDetails=strDetails+"Pet Description: "+petDetailModel!!.petDescription+"\n"
                    }
                    if(!petDetailModel!!.ownerNamer.equals(""))
                    {
                        strDetails=strDetails+"Owner Name : "+petDetailModel!!.firstName+" "+petDetailModel!!.lastName+"\n"
                    }
                    if(!petDetailModel!!.isPrivate.equals(null) && !petDetailModel!!.isPrivate.equals("no")  )
                    {
                        strDetails=strDetails+"Owner Number: "+petDetailModel!!.userNumber+"\n"
                    }
                    tv_moreFieldsValues.text=""+strDetails
                    if(petDetailModel!!.favourite.equals("yes"))
                        iv_favImg.setImageResource(R.drawable.ic_favorite_filled)

                    if(petDetailModel!!.favourite.equals("no"))
                        iv_favImg.setImageResource(R.drawable.ic_favorite_empty)
                    iv_favImg.setOnClickListener{

                        if(petDetailModel!!.favourite.equals("yes")) {
                            if (utilities.isConnected(this@PetDetailsActivity))
                                callChangeFavApi("remove")
                            else
                                utilities.snackBar(slider, "Please check internet ")
                        }
                        else{
                            if (utilities.isConnected(this@PetDetailsActivity))
                                callChangeFavApi("add")
                            else
                                utilities.snackBar(slider, "Please check internet ")

                        }
                    }


                } else {
                    GlobalData.showSnackbar(this@PetDetailsActivity as Activity, response.body()!!.messageType)
                }
            }

            override fun onFailure(call: Call<AllApiResponse.PetDetailRes>, t: Throwable) {
                // progress_bar.setVisibility(View.GONE);
                Toast.makeText(this@PetDetailsActivity, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show()
            }
        })

    }
    private fun callChangeFavApi(strAction: String) {
        Log.e(TAG,"callChangeFavApi   adsId="+petId+"   action="+strAction)
        var dialogBar=utilities.dialog(this@PetDetailsActivity)

        val call = service!!.addInFav(""+strAction ,""+ SharedPrefUtil.getUserId(this@PetDetailsActivity),""+ petId).enqueue(object : Callback<AllApiResponse.CommonRes> {
            override fun onResponse(
                    call: Call<AllApiResponse.CommonRes>,
                    response: Response<AllApiResponse.CommonRes>
            ) {
                Log.e("callAddInFavApi res", "" + Gson().toJson(response.body()))
                dialogBar.cancel()
                if (response.isSuccessful && (response.body()!!.status.equals("200"))) {
                    if(strAction.equals("add"))
                        iv_favImg.setImageResource(R.drawable.ic_favorite_filled)

                    if(strAction.equals("remove"))
                        iv_favImg.setImageResource(R.drawable.ic_favorite_empty)

                 } else {
                    //swipe_refresh.isRefreshing = false
                }
            }

            override fun onFailure(call: Call<AllApiResponse.CommonRes>, t: Throwable) {
                t.printStackTrace()
                dialogBar.cancel()

                //swipe_refresh.isRefreshing = false
            }
        })


    }

    override fun onBackPressed() {

        finish()
    }
}
