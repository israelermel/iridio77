package com.github.israelermel.iridio77.extensions

import com.github.israelermel.iridio77.utils.IRDimension
import java.awt.Dimension
import javax.swing.JPanel

fun JPanel.setupPreferredSizeDefault() {
    minimumSize = Dimension(IRDimension.Form.WIDTH_M, IRDimension.Form.HEIGHT_M)
    preferredSize = Dimension(IRDimension.Form.WIDTH_M, IRDimension.Form.HEIGHT_M)
}

fun JPanel.setupPreferredSizeLarge() {
    minimumSize = Dimension(IRDimension.Form.WIDTH_XL, IRDimension.Form.HEIGHT_XL)
    preferredSize = Dimension(IRDimension.Form.WIDTH_XL, IRDimension.Form.HEIGHT_XL)
}