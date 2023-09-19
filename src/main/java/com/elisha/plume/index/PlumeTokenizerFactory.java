package com.elisha.plume.index;

import org.apache.logging.log4j.Logger;
import org.apache.lucene.analysis.Tokenizer;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.IndexSettings;
import org.elasticsearch.index.analysis.AbstractTokenizerFactory;
import org.elasticsearch.index.analysis.TokenizerFactory;

import com.elisha.plume.core.Configuration;
import com.elisha.plume.core.PlumeTokenizer;
import com.elisha.plume.help.ESPluginLoggerFactory;

/**
 * @author tenlee
 * @date 2020/5/28
 */
public class PlumeTokenizerFactory extends AbstractTokenizerFactory {

    private static final Logger log = ESPluginLoggerFactory.getLogger(PlumeTokenizerFactory.class);

    private Configuration configuration;

    private String name;

    /**
     * 构造函数
     * @param indexSettings 索引配置
     * @param name 分析器或者分词器名称。如果是自定义分析器，则为自定义分析器名称
     * @param env es环境配置
     * @param settings 自定义分析器配置参数
     */
    public PlumeTokenizerFactory(IndexSettings indexSettings, String name, Environment env, Settings settings) {
        super(indexSettings,  null ,settings);
        this.name = name;
        configuration = new Configuration(indexSettings, name, env, settings);
//        log.info("[PlumeTokenizerFactory-PlumeTokenizerFactory] name={}", name);
    }

    public static TokenizerFactory getTokenizerFactory(IndexSettings indexSettings, Environment environment, String name, Settings settings) {
        return new PlumeTokenizerFactory(indexSettings, name, environment, settings);
    }

    @Override
    public Tokenizer create() {
        return new PlumeTokenizer(configuration);
    }
}
