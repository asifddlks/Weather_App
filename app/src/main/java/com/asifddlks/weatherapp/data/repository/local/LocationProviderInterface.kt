package com.asifddlks.weatherapp.data.repository.local

import com.asifddlks.weatherapp.data.models.LocationData
import com.asifddlks.weatherapp.utils.RequestCompleteListener


/**
 * Created by Asif Ahmed on 12/3/2024.
 * asifddlks@gmail.com
 */

interface LocationProviderInterface {
    fun getUserCurrentLocation(callback:RequestCompleteListener<LocationData>)
}