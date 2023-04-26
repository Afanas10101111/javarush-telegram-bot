package com.github.afanas10101111.jtb.client;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class JavarushClientProperties {
    public static final String JAVARUSH_URL = "https://javarush.ru/api/1.0/rest";
    public static final String GROUPS_PATH = "/groups";
    public static final String POSTS_PATH = "/posts";
    public static final String GROUP_KID_PARAM = "groupKid";
}
