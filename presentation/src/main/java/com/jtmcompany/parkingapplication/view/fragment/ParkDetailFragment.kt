package com.jtmcompany.parkingapplication.view.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jtmcompany.domain.model.ParkInfo
import com.jtmcompany.parkingapplication.R
import com.jtmcompany.parkingapplication.base.BaseFragment
import com.jtmcompany.parkingapplication.databinding.FragmentParkDetailBinding
import com.jtmcompany.parkingapplication.utils.Constatns
import com.jtmcompany.parkingapplication.utils.GeoDistanceManager
import com.kakao.sdk.navi.Constants
import com.kakao.sdk.navi.NaviClient
import com.kakao.sdk.navi.model.CoordType
import com.kakao.sdk.navi.model.Location
import com.kakao.sdk.navi.model.NaviOption
import com.skt.Tmap.TMapTapi


class ParkDetailFragment : BaseFragment<FragmentParkDetailBinding>(R.layout.fragment_park_detail),
    View.OnClickListener {
    private lateinit var parkInfo: ParkInfo
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLayout()
    }

    private fun initLayout() {
        arguments?.let {
            parkInfo = it.getSerializable(Constatns.KEY_SELECT_PARK_INFO) as ParkInfo
            binding.txtParkingLotName.text = parkInfo.prkplceNm
            binding.txtDistance.text = GeoDistanceManager.getDistanceStr(parkInfo.distance)
            binding.txtPrkplceSe.text = parkInfo.prkplceSe
            binding.txtPrkplceType.text = parkInfo.prkplceType
            binding.txtRdnmadr.text = parkInfo.rdnmadr
            binding.txtLnmadr.text = parkInfo.lnmadr
            binding.txtOperDay.text = parkInfo.operDay
            binding.txtWeekdayOperOpenHhmm.text = parkInfo.weekdayOperOpenHhmm
            binding.txtWeekdayOperColseHhmm.text = parkInfo.weekdayOperColseHhmm
            binding.txtSatOperOperOpenHhmm.text = parkInfo.satOperOperOpenHhmm
            binding.txtSatOperCloseHhmm.text = parkInfo.satOperCloseHhmm
            binding.txtHolidayOperOpenHhmm.text = parkInfo.holidayOperOpenHhmm
            binding.txtHolidayCloseOpenHhmm.text = parkInfo.holidayCloseOpenHhmm
            binding.txtParkingchrgeInfo.text = parkInfo.parkingchrgeInfo
            binding.txtBasicTime.text = parkInfo.basicTime
            binding.txtBasicCharge.text = parkInfo.basicCharge
            binding.txtAddUnitTime.text = parkInfo.addUnitTime
            binding.txtAddUnitCharge.text = parkInfo.addUnitCharge
            binding.txtDayCmmtktAdjTime.text = parkInfo.dayCmmtktAdjTime
            binding.txtDayCmmtkt.text = parkInfo.dayCmmtkt
            binding.txtMonthCmmtkt.text = parkInfo.monthCmmtkt
            binding.txtMetpay.text = parkInfo.metpay
            binding.txtSpcmnt.text = parkInfo.spcmnt
            binding.txtInstitutionNm.text = parkInfo.institutionNm
            binding.txtPhoneNumber.text = parkInfo.phoneNumber
        }

        binding.btnGetDirections.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnGetDirections -> {
                val bottomDialogFragment =NaviMenuBottomDialogFragment(parkInfo)
                activity?.let { bottomDialogFragment.show(it.supportFragmentManager,"") }
            }
        }
    }
}