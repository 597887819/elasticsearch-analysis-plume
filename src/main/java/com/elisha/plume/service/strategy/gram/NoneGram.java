package com.elisha.plume.service.strategy.gram;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;

import com.elisha.plume.core.Configuration;
import com.elisha.plume.core.Term;
import com.elisha.plume.help.ESPluginLoggerFactory;

/**
 * @author xiaogu
 * @date 2023/9/15 4:09 PM
 */
public class NoneGram extends AbstractPlumeGramStrategy {

    private static final Logger log = ESPluginLoggerFactory.getLogger(NoneGram.class);

    @Override
    public List<Term> tokenize(String line, Configuration configuration) {
        List<Term> resultList = new ArrayList<>();
        resultList.add(new Term(0, line.length()-1, line));
        return resultList;
    }
}
