package com.jtmcompany.parkingapplication.view.fragment

import android.content.Context
import android.inputmethodservice.InputMethodService
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.core.content.getSystemService
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.jtmcompany.domain.model.ParkInfo
import com.jtmcompany.parkingapplication.R
import com.jtmcompany.parkingapplication.base.BaseFragment
import com.jtmcompany.parkingapplication.databinding.FragmentParkSearchBinding
import com.jtmcompany.parkingapplication.utils.Constatns.KEY_USER_LATITUDE
import com.jtmcompany.parkingapplication.utils.Constatns.NONE_DISTANCE
import com.jtmcompany.parkingapplication.utils.GeoDistanceManager
import com.jtmcompany.parkingapplication.utils.TypeCheckManager
import com.jtmcompany.parkingapplication.view.ParkInfoViewModel


class ParkSearchFragment : BaseFragment<FragmentParkSearchBinding>(R.layout.fragment_park_search),
    View.OnClickListener {

    private val viewModel: ParkInfoViewModel by activityViewModels()
    private lateinit var searchResult: ArrayList<ParkInfo>
    private var userLatitude: Double = 0.0
    private var userLongitude: Double = 0.0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        initLayout()
        initObserver()

    }

    private fun initLayout() {
        searchResult = ArrayList()
        binding.spParkSection.adapter = getSpinnerAdpater(R.array.parking_lot_section)
        binding.spParkType.adapter = getSpinnerAdpater(R.array.parking_lot_type)
        binding.spParkCharge.adapter = getSpinnerAdpater(R.array.parking_charge_info)
        binding.btSearch.setOnClickListener(this)

        arguments?.let {
            userLatitude = it.getDouble(KEY_USER_LATITUDE)
            userLongitude = it.getDouble(KEY_USER_LATITUDE)
        }
    }

    private fun getSpinnerAdpater(resource: Int): ArrayAdapter<String> {
        val adapter = ArrayAdapter<String>(
            mContext,
            R.layout.spinner_item,
            resources.getStringArray(resource)
        )
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        return adapter
    }

    private fun initObserver() {
        viewModel.parkLocalList.observe(this, Observer { parkInfoList ->
            val keyword = binding.editKeword.text.toString()
            for (parkInfo in parkInfoList) {
                var isPassCondition = false //조건 부합 여부
                var isHasResult = false //검색 결과가 있는지

                //주차장 구분
                if(binding.spParkSection.selectedItemPosition>0){
                    parkInfo.prkplceSe?.let {
                        isPassCondition = it == binding.spParkSection.selectedItem.toString()
                    }
                }

                //주차장 유형
                if(binding.spParkType.selectedItemPosition>0){
                    parkInfo.prkplceType.let {
                        isPassCondition = it == binding.spParkType.selectedItem.toString()
                    }
                }

                //요금 정보
                if(binding.spParkCharge.selectedItemPosition>0){
                    parkInfo.parkingchrgeInfo.let {
                        isPassCondition = it == binding.spParkCharge.selectedItem.toString()
                    }
                }

                if(isPassCondition) {
                    parkInfo.prkplceNm?.let {//주차장 이름 체크
                        if (it.contains(keyword)) {
                            isHasResult = true
                        } else { //도로명 주소 또는 지번 주소 체크
                            if (!TextUtils.isEmpty(parkInfo.rdnmadr) //도로명 주소
                                && parkInfo.rdnmadr!!.contains(keyword)
                            ) {
                                isHasResult = true
                            } else if (!TextUtils.isEmpty(parkInfo.lnmadr) //지번 주소
                                && parkInfo.lnmadr!!.contains(keyword)
                            ) {
                                isHasResult = true
                            }
                        }
                    }
                }

                //결과 item list에 넣기
                if (isHasResult) {
                    //나와의 거리 계산
                    if (TypeCheckManager.isNumeric(parkInfo.latitude)
                        && TypeCheckManager.isNumeric(parkInfo.longitude)) {
                        parkInfo.distance = GeoDistanceManager.getDistance(
                            userLatitude,
                            userLongitude,
                            parkInfo.latitude!!.toDouble(),
                            parkInfo.longitude!!.toDouble()
                        )
                    }else{
                        //거리 계산 안됨
                        parkInfo.distance = NONE_DISTANCE.toDouble()
                    }
                    searchResult.add(parkInfo)
                }
            }
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.bt_search -> {
                val editKeyword = binding.editKeword
                val keyword = editKeyword.text.toString()
                val imm =
                    activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                //주차장 입력 체크
                if (TextUtils.isEmpty(keyword)) {
                    showToast(getString(R.string.msg_check_empty_park_))
                    editKeyword.requestFocus()
                    return
                }

                //주차장 자리 체크
                if (keyword.length < 2) {
                    showToast(getString(R.string.msg_check_word_count))
                    return
                }
                binding.layoutNoData.visibility = View.GONE
                //키보드 숨기기
                imm.hideSoftInputFromWindow(editKeyword.windowToken, 0)
                viewModel.requestLocalPark()
            }
        }
    }


}