package com.elisha.plume.service.strategy.gram;

import org.apache.logging.log4j.Logger;

import com.elisha.plume.help.ESPluginLoggerFactory;
import com.elisha.plume.help.TextUtility;

/**
 * @author xiaogu
 * @date 2023/9/19 11:00 AM
 */
public abstract class AbstractPlumeGramStrategy implements PlumeGramStrategy {

    private static final Logger log = ESPluginLoggerFactory.getLogger(AbstractPlumeGramStrategy.class);

    /**
     * 最小分词长度，默认1
     */
    protected int minGram = 1;

    /**
     * 最大分词长度，默认-1 表示文本最大长度
     */
    protected int maxGram = -1;

    @Override
    public PlumeGramStrategy init(String minGram, String maxGram) {
        if (!TextUtility.isBlank(minGram)) {
            this.minGram = Integer.parseInt(minGram);
        }
        if (!TextUtility.isBlank(maxGram)) {
            this.maxGram = Integer.parseInt(maxGram);
        }
        return this;
    }
}
