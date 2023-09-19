package com.elisha.elasticsearch.analysis;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.elisha.plume.core.Term;
import com.elisha.plume.service.strategy.gram.HGram;
import com.elisha.plume.service.strategy.gram.PlumeGramStrategy;

/**
 * @author xiaogu
 * @date 2023/9/18 10:53 AM
 */
public class HgramTest {

    @Test
    public void testProcess() {
        List<String> minGramList = new ArrayList<>();
        minGramList.add(null);
        minGramList.add("2");
        minGramList.add("2");
        minGramList.add(null);

        List<String> maxGramList = new ArrayList<>();
        maxGramList.add(null);
        maxGramList.add("6");
        maxGramList.add(null);
        maxGramList.add("7");

        for (int i = 0; i < minGramList.size(); i++) {
            PlumeGramStrategy strategy = new HGram().init(minGramList.get(i), maxGramList.get(i));
            List<Term> terms = strategy.tokenize("7310830ES", null);
            terms.forEach(term -> System.out.print(term.getText() + "(" + term.getOffset() + "," + term.getEnd() + ")"+ ","));
            System.out.println();
        }
    }
}
