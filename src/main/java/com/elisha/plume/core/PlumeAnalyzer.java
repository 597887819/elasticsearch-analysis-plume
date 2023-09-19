package com.elisha.plume.core;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;

/**
 * @author tenlee
 * @date 2020/5/28
 */
public final class PlumeAnalyzer extends Analyzer {

    private Configuration configuration;

    /**
     * Lucene Analyzer接口实现类
     *
     * @param configuration 配置
     */
    public PlumeAnalyzer(Configuration configuration) {
        super();
        this.configuration = configuration;
    }

    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        Tokenizer source = new PlumeTokenizer(configuration);
        return new TokenStreamComponents(source);
    }
}
