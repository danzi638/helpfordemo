package com.hyphenate.easeui.model;

import com.hyphenate.easeui.R;
import com.hyphenate.easeui.domain.EaseEmojicon;
import com.hyphenate.easeui.domain.EaseEmojiconGroupEntity;

import java.util.Arrays;

/**
 * Created by crx on 2017/7/5.
 */

public class EmojiconExampleGroupData {
    private static int[] icons=new int[]{
            R.drawable.biaoqing_1,
            R.drawable.biaoqing_2,
            R.drawable.biaoqing_3,
            R.drawable.biaoqing_4,
            R.drawable.biaoqing_5,
            R.drawable.biaoqing_6,
            R.drawable.biaoqing_7,
            R.drawable.biaoqing_8,
            R.drawable.biaoqing_9,
            R.drawable.biaoqing_10,
            R.drawable.biaoqing_11,
            R.drawable.biaoqing_12,
            R.drawable.biaoqing_13,
            R.drawable.biaoqing_14,
            R.drawable.biaoqing_15,
            R.drawable.biaoqing_16,
            R.drawable.biaoqing_17,
            R.drawable.biaoqing_18,
            R.drawable.biaoqing_19,
            R.drawable.biaoqing_20,
            R.drawable.biaoqing_21,
            R.drawable.biaoqing_22,
            R.drawable.biaoqing_23,
            R.drawable.biaoqing_24,
            R.drawable.biaoqing_25,
            R.drawable.biaoqing_26,
            R.drawable.biaoqing_27,
            R.drawable.biaoqing_28,
            R.drawable.biaoqing_29,
            R.drawable.biaoqing_30,
            R.drawable.biaoqing_31,
            R.drawable.biaoqing_32,
            R.drawable.biaoqing_33,
            R.drawable.biaoqing_34,
            R.drawable.biaoqing_35,
            R.drawable.biaoqing_36,
            R.drawable.biaoqing_37,
            R.drawable.biaoqing_38,
            R.drawable.biaoqing_39,
            R.drawable.biaoqing_40,
            R.drawable.biaoqing_41,
            R.drawable.biaoqing_42,
            R.drawable.biaoqing_43,
            R.drawable.biaoqing_44,
            R.drawable.biaoqing_45,
            R.drawable.biaoqing_46,
            R.drawable.biaoqing_47,
            R.drawable.biaoqing_48,
            R.drawable.biaoqing_49,
            R.drawable.biaoqing_50,
            R.drawable.biaoqing_51,
            R.drawable.biaoqing_52,
            R.drawable.biaoqing_53,
            R.drawable.biaoqing_54,
            R.drawable.biaoqing_55,

    };
    private static int[] bigIcons=new int[]{
            R.drawable.biaoqing_1,
            R.drawable.biaoqing_2,
            R.drawable.biaoqing_3,
            R.drawable.biaoqing_4,
            R.drawable.biaoqing_5,
            R.drawable.biaoqing_6,
            R.drawable.biaoqing_7,
            R.drawable.biaoqing_8,
            R.drawable.biaoqing_9,
            R.drawable.biaoqing_10,
            R.drawable.biaoqing_11,
            R.drawable.biaoqing_12,
            R.drawable.biaoqing_13,
            R.drawable.biaoqing_14,
            R.drawable.biaoqing_15,
            R.drawable.biaoqing_16,
            R.drawable.biaoqing_17,
            R.drawable.biaoqing_18,
            R.drawable.biaoqing_19,
            R.drawable.biaoqing_20,
            R.drawable.biaoqing_21,
            R.drawable.biaoqing_22,
            R.drawable.biaoqing_23,
            R.drawable.biaoqing_24,
            R.drawable.biaoqing_25,
            R.drawable.biaoqing_26,
            R.drawable.biaoqing_27,
            R.drawable.biaoqing_28,
            R.drawable.biaoqing_29,
            R.drawable.biaoqing_30,
            R.drawable.biaoqing_31,
            R.drawable.biaoqing_32,
            R.drawable.biaoqing_33,
            R.drawable.biaoqing_34,
            R.drawable.biaoqing_35,
            R.drawable.biaoqing_36,
            R.drawable.biaoqing_37,
            R.drawable.biaoqing_38,
            R.drawable.biaoqing_39,
            R.drawable.biaoqing_40,
            R.drawable.biaoqing_41,
            R.drawable.biaoqing_42,
            R.drawable.biaoqing_43,
            R.drawable.biaoqing_44,
            R.drawable.biaoqing_45,
            R.drawable.biaoqing_46,
            R.drawable.biaoqing_47,
            R.drawable.biaoqing_48,
            R.drawable.biaoqing_49,
            R.drawable.biaoqing_50,
            R.drawable.biaoqing_51,
            R.drawable.biaoqing_52,
            R.drawable.biaoqing_53,
            R.drawable.biaoqing_54,
            R.drawable.biaoqing_55,
    };


    private static final EaseEmojiconGroupEntity DATA = createData();

    private static EaseEmojiconGroupEntity createData(){
        EaseEmojiconGroupEntity emojiconGroupEntity = new EaseEmojiconGroupEntity();
        EaseEmojicon[] datas = new EaseEmojicon[icons.length];
        for(int i = 0; i < icons.length; i++){
            datas[i] = new EaseEmojicon(icons[i], null, EaseEmojicon.Type.BIG_EXPRESSION);
            datas[i].setBigIcon(bigIcons[i]);
//            you can replace this to any you want

            datas[i].setIdentityCode("em"+ (1000+i+1));
        }
        emojiconGroupEntity.setEmojiconList(Arrays.asList(datas));
        emojiconGroupEntity.setIcon(R.drawable.ee_2);
        emojiconGroupEntity.setType(EaseEmojicon.Type.BIG_EXPRESSION);
        return emojiconGroupEntity;
    }


    public static EaseEmojiconGroupEntity getData(){
        return DATA;
    }

}
