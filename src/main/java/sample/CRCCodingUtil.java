package sample;

public class CRCCodingUtil {

    private static final String divider = "1011";

    public static String getCRC(String data, String additionalBits){
        if (data == null || data.isEmpty()){
            throw new IllegalArgumentException("The passed string cannot be null or empty");
        }

        StringBuilder builder = new StringBuilder(data);
        builder.append(additionalBits);
        while (builder.charAt(0) == '0' && builder.length() > 0){
            builder.deleteCharAt(0);
        }
        if (builder.length() == 0){
            return "";
        }

        int length = 0;
        while (length != 3){
            for (int i = 0; i < 4; i++){
                if (builder.charAt(i) != CRCCodingUtil.divider.charAt(i)){
                    builder.setCharAt(i, '1');
                }else {
                    builder.setCharAt(i, '0');
                }
            }
            while (builder.charAt(0) == '0'){
                if (builder.length() == 3){
                    return new String(builder);
                }
                builder.deleteCharAt(0);
            }
            length = builder.length();
        }

        return new String(builder);
    }

    public static String formDataToSend(String sourceData){
        if (sourceData == null || sourceData.isEmpty()){
            throw new IllegalArgumentException("The passed string cannot be null or empty");
        }
        StringBuilder data = new StringBuilder();
        int bytesAmount = sourceData.length()/8;
        for (int i = 0; i < bytesAmount; i++){
            String byteSubstring = sourceData.substring(0, 8);
            String crc = getCRC(byteSubstring, "000");
            data.append(byteSubstring);
            data.append(crc);
            sourceData = sourceData.substring(8);
        }
        return new String(data);
    }

    public static boolean checkCRC(String data){
        if (data == null || data.isEmpty()){
            throw new IllegalArgumentException("The passed string cannot be null or empty");
        }
        if (data.length() % 11 != 0){
            return false;
        }
        int blocks = data.length() / 11; //11 = 8(byte size) + 3 (byte crc)
        for (int i = 0; i < blocks; i++){
            String byteSubstring = data.substring(0, 8);
            String crc = data.substring(8, 11);
            String realCRC = getCRC(byteSubstring, crc);
            if(realCRC.contains("1")){
                return false;
            }
            data = data.substring(11);
        }
        return true;
    }

    public static String removeCRS(String data){
        if (data == null || data.isEmpty()){
            throw new IllegalArgumentException("The passed string cannot be null or empty");
        }
        int blocks = data.length() / 11; //11 = 8(byte size) + 3 (byte crc)
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < blocks; i++){
            builder.append(data.substring(0,8));
            data = data.substring(11);
        }
        return new String(builder);
    }
}
