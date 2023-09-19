package com.elisha.elasticsearch.analysis;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.elisha.plume.core.Term;
import com.elisha.plume.service.strategy.gram.PlumeGramStrategy;
import com.elisha.plume.service.strategy.gram.TGram;

/**
 * @author xiaogu
 * @date 2023/9/18 10:53 AM
 */
public class TgramTest {

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
            PlumeGramStrategy strategy = new TGram().init(minGramList.get(i), maxGramList.get(i));
            List<Term> terms = strategy.tokenize("7310830ES", null);
            terms.forEach(term -> System.out.print(term.getText() + "(" + term.getOffset() + "," + term.getEnd() + ")"+ ","));
            System.out.println();
        }
    }

    /**
     * [2，6]
     */
    @Test
    public void name() {
        String str = "1234567890";
        /*
        总循环次数 5 = 6-2 + 1
        10 - 2
        10 - 3
        10 - 4
        10 - 5
        10 - 6
         */
        System.out.println(str.substring(8, 10));
        System.out.println(str.substring(7, 10));
        System.out.println(str.substring(6, 10));
        System.out.println(str.substring(5, 10));
        System.out.println(str.substring(4, 10));
    }
}
