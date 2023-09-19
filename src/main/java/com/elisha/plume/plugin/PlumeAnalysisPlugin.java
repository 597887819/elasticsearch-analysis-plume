package com.elisha.plume.plugin;

import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.elasticsearch.index.analysis.AnalyzerProvider;
import org.elasticsearch.index.analysis.TokenizerFactory;
import org.elasticsearch.indices.analysis.AnalysisModule;
import org.elasticsearch.plugins.AnalysisPlugin;
import org.elasticsearch.plugins.Plugin;

import com.elisha.plume.index.PlumeAnalyzerProvider;
import com.elisha.plume.index.PlumeTokenizerFactory;

/**
 * @author tenlee
 * @date 2020/5/28
 */
public class PlumeAnalysisPlugin extends Plugin implements AnalysisPlugin {

    public static final String PLUGIN_NAME = "plume";

    @Override
    public Map<String, AnalysisModule.AnalysisProvider<TokenizerFactory>> getTokenizers() {
        Map<String, AnalysisModule.AnalysisProvider<TokenizerFactory>> extra = new HashMap<>();

//        for (AnalyzerTypeEnum typeEnum : AnalyzerTypeEnum.values()) {
//            extra.put(typeEnum.getCode(), PlumeTokenizerFactory::getTokenizerFactory);
//        }
        extra.put(PLUGIN_NAME, PlumeTokenizerFactory::getTokenizerFactory);
        return extra;
    }

    @Override
    public Map<String, AnalysisModule.AnalysisProvider<AnalyzerProvider<? extends Analyzer>>> getAnalyzers() {
        Map<String, AnalysisModule.AnalysisProvider<AnalyzerProvider<? extends Analyzer>>> extra = new HashMap<>();

//        for (AnalyzerTypeEnum typeEnum : AnalyzerTypeEnum.values()) {
//            extra.put(typeEnum.getCode(), PlumeAnalyzerProvider::getAnalyzerProvider);
//        }
        extra.put(PLUGIN_NAME, PlumeAnalyzerProvider::getAnalyzerProvider);
        return extra;
    }
}
