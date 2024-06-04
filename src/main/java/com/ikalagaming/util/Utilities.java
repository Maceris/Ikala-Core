package com.ikalagaming.util;

import com.ikalagaming.localization.Localization;

import lombok.Getter;

import java.util.ResourceBundle;

/** Just used for common localization. */
class Utilities {
    @Getter
    private static final ResourceBundle resourceBundle =
            ResourceBundle.getBundle("com.ikalagaming.util.Utilities", Localization.getLocale());
}
