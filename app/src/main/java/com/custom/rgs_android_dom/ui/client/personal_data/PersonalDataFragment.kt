package com.custom.rgs_android_dom.ui.client.personal_data

import android.os.Bundle
import android.view.View
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.data.network.url.GlideUrlProvider
import com.custom.rgs_android_dom.databinding.FragmentPersonalDataBinding
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.client.personal_data.add_photo.AddPhotoFragment
import com.custom.rgs_android_dom.utils.*

class PersonalDataFragment : BaseFragment<PersonalDataViewModel, FragmentPersonalDataBinding>(R.layout.fragment_personal_data) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backImageView.setOnDebouncedClickListener {
            viewModel.onBackClick()
        }

        binding.editImageView.setOnDebouncedClickListener {
            viewModel.onEditClick()
        }

        binding.editPhotoTextView.setOnDebouncedClickListener {
            val addPhotoFragment = AddPhotoFragment()
            addPhotoFragment.show(childFragmentManager, addPhotoFragment.TAG)
        }

        subscribe(viewModel.personalDataObserver){personalData->
            if (personalData.name.isEmpty()){
                binding.nameTextView.setNoValue()
            } else {
                binding.nameTextView.setValue(personalData.name)
            }

            if (personalData.birthday.isEmpty()){
                binding.birthdayTextView.setNoValue()
            } else {
                binding.birthdayTextView.setValue(personalData.birthday)
            }

            if (personalData.gender.isEmpty()){
                binding.genderTextView.setNoValue()
            } else {
                binding.genderTextView.setValue(personalData.gender)
            }

            if (personalData.passport.isEmpty()){
                binding.passportTextView.setNoValue()
            } else {
                binding.passportTextView.setValue(personalData.passport)
            }

            if (personalData.phone.isEmpty()){
                binding.phoneTextView.setNoValue()
            } else {
                binding.phoneTextView.setValue(personalData.phone)
            }

            if (personalData.additionalPhone.isEmpty()){
                binding.additionalPhoneTextView.setNoValue()
            } else {
                binding.additionalPhoneTextView.setValue(personalData.additionalPhone)
            }

            if (personalData.email.isEmpty()){
                binding.emailTextView.setNoValue()
            } else {
                binding.emailTextView.setValue(personalData.email)
            }

            if (personalData.avatar.isEmpty()){
                binding.avatarImageView.gone()
                binding.avatarStubImageView.visible()
                binding.noAvatarTextView.visible()
            } else {
                binding.avatarImageView.visible()
                binding.avatarStubImageView.gone()
                binding.noAvatarTextView.gone()

                GlideApp.with(requireContext())
                    .load(GlideUrlProvider.makeHeadersGlideUrl(personalData.avatar))
                    .circleCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(binding.avatarImageView)
            }

        }
    }

}