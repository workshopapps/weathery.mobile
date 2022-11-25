package com.gear.weathery.account

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import com.gear.weathery.R

public class ProfileFragmentDirections private constructor() {
  public companion object {
    public fun actionProfileFragmentToPersonalDataFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_profileFragment_to_personalDataFragment)

    public fun actionProfileFragmentToUploadPictureFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_profileFragment_to_uploadPictureFragment)
  }
}
