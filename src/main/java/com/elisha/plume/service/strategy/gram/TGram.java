package com.elisha.plume.service.strategy.gram;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.apache.logging.log4j.Logger;

import com.elisha.plume.core.Configuration;
import com.elisha.plume.core.Term;
import com.elisha.plume.help.ESPluginLoggerFactory;

/**
 * @author xiaogu
 * @date 2023/9/15 3:49 PM
 * 尾部分词
 * 如：7310830ES，拆分为 S,ES,0ES,30ES...
 */
public class TGram extends AbstractPlumeGramStrategy {

    private static final Logger log = ESPluginLoggerFactory.getLogger(TGram.class);

    @Override
    public List<Term> tokenize(String line, Configuration configuration) {
        if (null == line || Objects.equals("", line)
                || Objects.equals("", line.trim())) {
            return Collections.EMPTY_LIST;
        }
        List<Term> terms = new ArrayList<>();
        int len = line.length();
        int maxLen = maxGram == -1 ? len : maxGram;
        maxLen = Math.min(len, maxLen);
        int fromIndex = len - maxLen;
        int loopTime = maxLen - minGram + 1;
        int toIndex = len-1;
        while (loopTime > 0) {
//            log.info("[Tgram-process] line={}, fromIndex={}, toIndex={} ", line, fromIndex, toIndex);
            terms.add(new Term(fromIndex, toIndex, line.substring(fromIndex)));
            fromIndex++;
            loopTime--;
        }
        return terms;
    }
}
