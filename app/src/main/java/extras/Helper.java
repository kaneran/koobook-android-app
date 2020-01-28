package extras;

import android.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Helper {

    //As some of the string will be empty, converting it to null will thrown an exception and hence why I created this method to handle this
    public double convertStringToDouble(String str){
        try{
            return Double.parseDouble(str);
        } catch(Exception e){
            return 0;

        }
    }

    public int convertStringToInt(String str){
        try{
            return Integer.parseInt(str);
        } catch(Exception e){
            return 0;

        }
    }

    //Some of the data fields will be empty and this was marked by "*"
    // if it does contain this symbol then return an empty string
    public String checkIfBookDataAttributeNull(String str){
        if (str.equals("*")){
            return "";
        } else{
            return str;
        }

    }

    //Goes through each unique string in the list and gets the number of occurences within the string and both values are put into the hashmap
    //Credit to Jahroy for solution on getting uniques values from string list https://stackoverflow.com/questions/13429119/get-unique-values-from-arraylist-in-java
    //Credit to Lars A for solution on getting the number occurence of a string in the list  https://stackoverflow.com/questions/505928/how-to-count-the-number-of-occurrences-of-an-element-in-a-list
    public HashMap<String, Integer> getOccurencesOfStringList(List<String> stringList){
        HashMap<String, Integer> hashMap = new HashMap<>();
        Set<String> uniqueStrings= new HashSet<>(stringList);
        for(String uniqueString: uniqueStrings){
            int occurences = Collections.frequency(stringList, uniqueString);
            hashMap.put(uniqueString, occurences);
        }
        return hashMap;
    }

    //Credit to Evgeniy D for solution on sorting the values in a hashmap https://stackoverflow.com/questions/21054415/how-to-sort-a-hashmap-by-the-integer-value
    public Object[] sortHashMapBasedOnKeyValue(HashMap<String, Integer> hashMap){
        Object[] a = hashMap.entrySet().toArray();
        Arrays.sort(a, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Map.Entry<String, Integer>) o2).getValue()
                        .compareTo(((Map.Entry<String, Integer>) o1).getValue());
            }
        });
        return a;
    }

    public List<Pair<String,Integer>> getTopPairs(Object[] a){
        List<Pair<String, Integer>> topPairs = new ArrayList<>();
        if(a.length <6){
            for(int i=0; i<a.length; i++){
                String authorName = ((Map.Entry<String, Integer>) a[i]).getKey();
                int occurences = ((Map.Entry<String, Integer>) a[i]).getValue();
                Pair<String, Integer> pair = new Pair<>(authorName,occurences);
                topPairs.add(pair);
            }

        } else {
            for (int i = 0; i < 5; i++) {
                String authorName = ((Map.Entry<String, Integer>) a[i]).getKey();
                int occurences = ((Map.Entry<String, Integer>) a[i]).getValue();
                Pair<String, Integer> pair = new Pair<>(authorName, occurences);
                topPairs.add(pair);
            }
        }
        return topPairs;
    }

    public List<String> formatBarChartColumns(List<Pair<String,Integer>> pairs){
        List<String> formattedBarChartColumns = new ArrayList<>();
        for(Pair<String,Integer> pair: pairs){
            String[] names = pair.first.split(" ");
            String formattedString = names[0] + "\n" + names[1];
            formattedBarChartColumns.add(formattedString);
        }
        return formattedBarChartColumns;

    }

    //This will only return the list where all values are valid i.e not containing numbers
    public List<String> getValidValuesFromList(List<String> list){
        List<String> validList = new ArrayList<>();
        for(String string: list){
            if(!string.matches(".*\\d.*")){
                validList.add(string);
            }
        }
        return validList;
    }



}
