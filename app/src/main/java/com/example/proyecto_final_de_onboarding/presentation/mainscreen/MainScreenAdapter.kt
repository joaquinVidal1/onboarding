package com.example.proyecto_final_de_onboarding.presentation.mainscreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proyecto_final_de_onboarding.Item
import com.example.proyecto_final_de_onboarding.Kind
import com.example.proyecto_final_de_onboarding.R
import com.example.proyecto_final_de_onboarding.domain.model.ScreenListItem
import com.example.proyecto_final_de_onboarding.databinding.ListItemMainScreenBinding
import com.example.proyecto_final_de_onboarding.databinding.ListKindMainScreenBinding
import java.math.RoundingMode
import java.text.DecimalFormat


private const val ITEM_VIEW_TYPE_SECTION_CONTENT = 1
private const val ITEM_VIEW_TYPE_SECTION_HEADER = 0

