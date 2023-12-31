package com.jtmcompany.parkingapplication.view.fragment

import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.jtmcompany.parkingapplication.R
import com.jtmcompany.parkingapplication.base.BaseFragment
import com.jtmcompany.parkingapplication.databinding.FragmentSplashBinding
import com.jtmcompany.parkingapplication.utils.Constatns
import com.jtmcompany.parkingapplication.view.ParkInfoViewModel

class SplashFragment : BaseFragment<FragmentSplashBinding>(R.layout.fragment_splash) {

    private val viewModel : ParkInfoViewModel by activityViewModels()
    private val perRequestCode = 1000
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel

        Handler(Looper.getMainLooper()).postDelayed({
            if(!checkPermission()){
                requestPermission()
            }else{
                findNavController().navigate(R.id.action_splashFragment_to_parkLocationFragment)
            }
        }, Constatns.LOADING_DELAY)
    }

    private fun requestPermission(){
        requestPermissions(
            arrayOf(
                android.Manifest.permission.INTERNET,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ), perRequestCode
        )
    }
    private fun checkPermission(): Boolean {
        val permission1 = checkSelfPermission(mContext, android.Manifest.permission.INTERNET)
        val permission2 = checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION)
        val permission3 = checkSelfPermission(mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION)

        return !(permission1 == PackageManager.PERMISSION_DENIED ||
                permission2 == PackageManager.PERMISSION_DENIED ||
                permission3 == PackageManager.PERMISSION_DENIED)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == perRequestCode) {
            var isAllPermissionGranted = true
            for (result in grantResults) { //모든 퍼미션을 허용했는지 체크
                if (result != PackageManager.PERMISSION_GRANTED) {
                    isAllPermissionGranted = false
                    break
                }
            }

            //권한 체크에 동의를 하지 않으면 안드로이드 종료
            if (!isAllPermissionGranted) {
                showToast(getString(R.string.msg_alert_need_permission))
                activity?.finish()
            }else{
                findNavController().navigate(R.id.action_splashFragment_to_parkLocationFragment)
            }
        }
    }
}