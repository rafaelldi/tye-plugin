package com.github.rafaelldi.tyeplugin.lang

import com.intellij.lang.Language

class TyeLanguage : Language("Tye") {
    companion object {
        @JvmStatic
        val INSTANCE = TyeLanguage()
    }
}
