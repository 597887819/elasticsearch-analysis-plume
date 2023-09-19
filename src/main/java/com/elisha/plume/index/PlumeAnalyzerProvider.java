package com.elisha.plume.index;

import com.elisha.plume.core.Configuration;
import com.elisha.plume.core.PlumeAnalyzer;
import org.apache.lucene.analysis.Analyzer;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.IndexSettings;
import org.elasticsearch.index.analysis.AbstractIndexAnalyzerProvider;
import org.elasticsearch.index.analysis.AnalyzerProvider;

/**
 * @author tenlee
 * @date 2020/5/28
 */
public class PlumeAnalyzerProvider extends AbstractIndexAnalyzerProvider<PlumeAnalyzer> {

    private final PlumeAnalyzer analyzer;

    /**
     * Constructs a new analyzer component, with the index name and its settings and the analyzer name.
     *
     * @param indexSettings the settings and the name of the index
     * @param name          The analyzer name
     * @param settings
     */
    public PlumeAnalyzerProvider(IndexSettings indexSettings, Environment env, String name, Settings settings, boolean indexMode) {
        super(indexSettings, name, settings);
        Configuration configuration = new Configuration(indexSettings, name, env, settings).setIndexMode(indexMode);

        analyzer = new PlumeAnalyzer(configuration);
    }

    public static AnalyzerProvider<? extends Analyzer> getAnalyzerProvider(IndexSettings indexSettings, Environment env, String name, Settings settings) {
        return new PlumeAnalyzerProvider(indexSettings, env, name, settings, false);
    }


    @Override
    public PlumeAnalyzer get() {
        return analyzer;
    }
}
