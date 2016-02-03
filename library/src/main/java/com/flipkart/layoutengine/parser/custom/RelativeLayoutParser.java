package com.flipkart.layoutengine.parser.custom;


import com.flipkart.layoutengine.parser.Attributes;
import com.flipkart.layoutengine.parser.ParseHelper;
import com.flipkart.layoutengine.parser.Parser;
import com.flipkart.layoutengine.parser.WrappableParser;
import com.flipkart.layoutengine.processor.StringAttributeProcessor;
import com.flipkart.layoutengine.view.RelativeLayout;

/**
 * Created by kirankumar on 10/07/14.
 */
public class RelativeLayoutParser<T extends RelativeLayout> extends WrappableParser<T> {

    public RelativeLayoutParser(Parser<T> parentParser) {
        super(RelativeLayout.class, parentParser);
    }

    @Override
    protected void prepareHandlers() {
        super.prepareHandlers();
        addHandler(Attributes.View.Gravity, new StringAttributeProcessor<T>() {

            @Override
            public void handle(String attributeKey, String attributeValue, T view) {
                view.setGravity(ParseHelper.parseGravity(attributeValue));
            }
        });
    }
}
