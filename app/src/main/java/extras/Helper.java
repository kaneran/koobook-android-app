package extras;

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



}
