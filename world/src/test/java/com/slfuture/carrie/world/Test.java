package com.slfuture.carrie.world;

import com.slfuture.carrie.base.logic.grammar.SymbolLogicalGrammar;
import com.slfuture.carrie.base.logic.grammar.WordLogicalGrammar;
import com.slfuture.carrie.world.relation.Condition;
import com.slfuture.carrie.world.relation.prepare.PropertyPrepare;

/**
 * 测试类
 */
public class Test {
    public static void main(String[] argv) {
        String text = "property1 = (long)1 && property2 = (string)abc || property3 = asb";
        Condition x = Condition.build(text, new SymbolLogicalGrammar());
        System.out.println(x.toString(new WordLogicalGrammar()));

        Condition a = new Condition();
        a.prepareSelf = new PropertyPrepare("property1");
        a.target = "value1";
        Condition b = new Condition();
        b.prepareSelf = new PropertyPrepare("property2");
        b.target = Integer.valueOf("2");
        a.put(true, b);

        Condition c = new Condition();
        c.prepareSelf = new PropertyPrepare("property1");
        c.target = "value1";
        Condition d = new Condition();
        d.prepareSelf = new PropertyPrepare("property2");
        d.target = Integer.valueOf("3");
        c.put(true, d);

        System.out.println(a.equalsIgnoreTarget(c));
        System.out.println(a.equals(c));
        System.out.println(a.toString(new WordLogicalGrammar()));
        return;
    }
}
