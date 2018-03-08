public class CRC{

        String generatorPolynomial = "100011101";
        String payload ="";
        public CRC (){
        }
    public boolean validateData(String inputData){
        String shiftRegister = "";
        char mostSiginificantBit;

        for(int i = 1; i < generatorPolynomial.length(); i++){
            shiftRegister += "0";
        }

        for(int position = 0; position < inputData.length(); position++){
            mostSiginificantBit = shiftRegister.charAt(0);

            shiftRegister = leftShiftRegister(shiftRegister, inputData.charAt(position));

            if(mostSiginificantBit == '1'){
                shiftRegister = XORList(shiftRegister, generatorPolynomial.substring(1));
            }
        }
        if (shiftRegister.equals("00000000")){
            return true;
        }
        return false;
    }

    public String computeCRC(String inputData){
        //Let m be the length of the generator polynomial.
        //Append m - 1 zero bits to the input string
        //The shift register is initialized to be a string of m - 1 zero bits

        String shiftRegister = "";
        char mostSiginificantBit;

        for(int i = 1; i < this.generatorPolynomial.length(); i++){
            inputData = inputData + "0";
            shiftRegister += "0";
        }

        for(int position = 0; position < inputData.length(); position++){
            mostSiginificantBit = shiftRegister.charAt(0);

            shiftRegister = leftShiftRegister(shiftRegister, inputData.charAt(position));

            if(mostSiginificantBit == '1'){
                shiftRegister = XORList(shiftRegister, this.generatorPolynomial.substring(1));
            }

        }
        return shiftRegister;
    }

    public static String leftShiftRegister(String register, char leastSignificantBit){

        StringBuilder myBuilder = new StringBuilder(register);

        for(int i = 0; i < register.length() - 1; i++){
            myBuilder.setCharAt(i, myBuilder.charAt(i + 1));
        }

        myBuilder.setCharAt(myBuilder.length() - 1, leastSignificantBit);

        return myBuilder.toString();
    }

    public static String XORList(String word1, String word2){
        String result = "";

        for(int i = 0; i < word1.length(); i++){
            result += XOR(word1.charAt(i), word2.charAt(i));
        }

        return result;
    }

    public static char XOR(char value1, char value2){
        if(value1 == value2)
            return '0';
        else return '1';
    }
}