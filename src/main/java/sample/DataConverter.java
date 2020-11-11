package sample;

public class DataConverter {
    public static String toBinaryFormat(String data){
        if (data == null || data.isEmpty()){
            throw new IllegalArgumentException("The passed string cannot be null or empty");
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < data.length(); i++){
            stringBuilder.append(String.format("%8s", Integer.toBinaryString(data.charAt(i))).replace(' ', '0'));
        }
        return new String(stringBuilder);
    }

    public static String toNormalFormat(String data){
        int byteCounter = data.length()/8;
        StringBuilder resultBuilder = new StringBuilder();
        for (int i = 0; i < byteCounter; i++){
            String substr = data.substring(0, 8);
            resultBuilder.append((char) Integer.parseInt(substr, 2));
            data = data.substring(8);
        }
        int i;
        return new String(resultBuilder);
    }
}
