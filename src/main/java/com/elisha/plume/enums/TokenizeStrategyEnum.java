package com.elisha.plume.enums;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * @author xiaogu
 * @date 2023/9/15 2:36 PM
 * 分词（语法标记）策略
 */
public enum TokenizeStrategyEnum {
    /**
     * 即 tail-gram 表示从尾部开始分词 可用于尾部匹配 如mysql 的 like "%xxx"
     * 如：SE1586 被分为 [6,86,586,1586,E1586,SE1586]
     */
    T_GRAM("t_gram"),

    /**
     * 即 head-gram 表示从头部开始分词 可用于头部匹配 如mysql 的 like "xxx%"
     * 如：SE1586 被分为 [S,SE,SE1,SE15,SE158,SE1586]
     */
    H_GRAM("h_gram"),

    /**
     * 不分词
     */
    NONE_GRAM("none_gram");

    private final String code;

    TokenizeStrategyEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    /**
     * 通过编码获取
     * @param code
     * @return
     */
    public static TokenizeStrategyEnum getByCode(String code) {
        if (null == code || Objects.equals("", code) || Objects.equals("", code.trim())) {
            return TokenizeStrategyEnum.NONE_GRAM;
        }
        return Stream.of(TokenizeStrategyEnum.values()).filter(anEnum -> Objects.equals(anEnum.getCode(), code)).findAny().orElse(TokenizeStrategyEnum.NONE_GRAM);
    }
}
