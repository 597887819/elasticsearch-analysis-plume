package com.elisha.plume.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.Logger;
import org.elasticsearch.common.settings.Settings;

import com.elisha.plume.core.Configuration;
import com.elisha.plume.core.Term;
import com.elisha.plume.enums.TokenizeStrategyEnum;
import com.elisha.plume.help.ESPluginLoggerFactory;
import com.elisha.plume.service.strategy.gram.HGram;
import com.elisha.plume.service.strategy.gram.NoneGram;
import com.elisha.plume.service.strategy.gram.PlumeGramStrategy;
import com.elisha.plume.service.strategy.gram.TGram;

/**
 * @author tenlee
 * @date 2020/7/7
 */
public class PlumeTokenizerService {

    private static final Logger log = ESPluginLoggerFactory.getLogger(PlumeTokenizerService.class);

    private Map<TokenizeStrategyEnum, PlumeGramStrategy> strategyMap = new HashMap<>();

    public PlumeTokenizerService() {
        buildStrategyMap();
    }

    private void buildStrategyMap() {
        strategyMap.put(TokenizeStrategyEnum.T_GRAM, new TGram());
        strategyMap.put(TokenizeStrategyEnum.H_GRAM, new HGram());
        strategyMap.put(TokenizeStrategyEnum.NONE_GRAM, new NoneGram());
    }

    public List<Term> getTextTokenizer(String line, Configuration configuration) {
        PlumeGramStrategy strategy = getInitStrategy(configuration);
        return strategy.tokenize(line, configuration);
    }

    private PlumeGramStrategy getInitStrategy(Configuration configuration) {
        String pluginName = configuration.getName();
        Settings settings = configuration.getIndexSettings().getSettings().getAsSettings("index.analysis.tokenizer." + pluginName);
        Map<String, String> properties = settings.keySet().stream().collect(Collectors.toMap(k -> k, settings::get));
        String strategy = properties.get("strategy");
        String minGram = properties.get("min_gram");
        String maxGram = properties.get("max_gram");
//        log.info("[PlumeTokenizerService-getInitedStrategy] pluginName={}, strategy={}, minGram={}, maxGram", pluginName, strategy, minGram, maxGram);
        TokenizeStrategyEnum strategyEnum = TokenizeStrategyEnum.getByCode(strategy);
        return strategyMap.get(strategyEnum).init(minGram, maxGram);
    }
}
