package com.elisha.plume.service.strategy.gram;

import java.util.List;

import com.elisha.plume.core.Configuration;
import com.elisha.plume.core.Term;

/**
 * @author xiaogu
 * @date 2023/9/15 3:55 PM
 * 窗口分词策略
 */
public interface PlumeGramStrategy {

    /**
     * 初始化
     * @return
     */
    PlumeGramStrategy init(String minGram, String maxGram);

    /**
     * 分词
     * @param line
     * @param configuration     配置信息
     * @return
     */
    List<Term> tokenize(String line, Configuration configuration);
}
