package com.manh.test.util.dataloader

import java.util.regex.Matcher
import java.util.regex.Pattern

class ReplaceDynamicValues {

    final String START_STRING = '${'
        final String END_STRING = "}";
        final String DYNAMIC_REGEX_EXP = /\$\\{([a-zA-Z_#0-9]{1,})\\}/

        TreeMap<String, String> map = new TreeMap<String, String>();

        ReplaceDynamicValues(TreeMap<String, String> map){
            this.map = map;
        }

        public Map<String, String> replaceDynamicContents(){

            for(Map.Entry<String, String> entry : map.entrySet()){
                if(entry.getValue().contains(START_STRING)){
                    List<String> multiDynamic = matchPatternMultiple(entry.getValue());
                    String value = "";
                    for(String str : multiDynamic){
                        value += getDynamicValue(str);
                    }
                    map.put(entry.getKey(), value);
                }
            }
            return map;
        }

        private String getDynamicValue(String dynamicValue){

            dynamicValue = matchPattern(dynamicValue);
            String val = map.get(dynamicValue);

            if(val.contains(START_STRING)){
                return getDynamicValue(val);
            } else{
                return val;
            }
        }

        private String matchPattern(String dynamicValue){

            Pattern p = Pattern.compile(DYNAMIC_REGEX_EXP);
            Matcher m = p.matcher(dynamicValue);
            if(m.find()) {
                return m.group(1);
            }
            return "pattern not found";
        }

        private List<String> matchPatternMultiple(String dynamicValue){

            Pattern p = Pattern.compile(DYNAMIC_REGEX_EXP);
            Matcher m = p.matcher(dynamicValue);
            List<String> list = new ArrayList<String>();
            while(m.find()) {
                list.add(m.group());
            }
            return list;
        }
}
