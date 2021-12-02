package com.github.rafaelldi.tyeplugin.settings

import com.intellij.ui.JBColor
import com.intellij.ui.components.JBTextField
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.RenderingHints
import kotlin.math.abs

class JBTextFieldWithSecondaryValue : JBTextField() {
    private var secondaryValue: String? = null

    fun setSecondaryValue(value: String) {
        if (secondaryValue == value)
            return

        secondaryValue = value
        repaint()
    }

    override fun paintComponent(g: Graphics?) {
        super.paintComponent(g)

        if (g == null) return
        val valueCharArray = secondaryValue?.toCharArray() ?: return

        val color = g.color
        val font = g.font

        g.color = JBColor.GRAY
        (g as Graphics2D).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        val metrics = g.fontMetrics
        val x = abs(width - metrics.charsWidth(valueCharArray, 0, valueCharArray.size) - 10)
        val y = abs(height - metrics.height) / 2 + metrics.ascent
        g.drawChars(valueCharArray, 0, valueCharArray.size, x, y)

        g.color = color
        g.font = font
    }
}