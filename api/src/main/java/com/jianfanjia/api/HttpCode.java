package com.jianfanjia.api;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jyz on 16/3/1.
 */
public class HttpCode {
    public static final Integer HTTP_FORBIDDEN_CODE = 403;
    public static final String HTTP_FORBIDDEN_MSG = "";

    public static final Integer NO_NETWORK_ERROR_CODE = -1;
    public static final String NO_NETWORK_ERROR_MSG = "没有网络";

    public static final Integer SAVE_FILE_ERROR_CODE = -2;
    public static final String SAVE_FILE_ERROR_MSG = "文件无法保存";




    public static final Map<Integer, String> CODE_2_MSG = new HashMap<Integer, String>();
    {
        CODE_2_MSG.put(HTTP_FORBIDDEN_CODE, HTTP_FORBIDDEN_MSG);
        CODE_2_MSG.put(NO_NETWORK_ERROR_CODE, NO_NETWORK_ERROR_MSG);
        CODE_2_MSG.put(SAVE_FILE_ERROR_CODE, SAVE_FILE_ERROR_MSG);
    }

    public static String getMsg(Integer code) {
        String msg = CODE_2_MSG.get(code);
        return msg == null ? "网络异常" : msg;
    }
}
