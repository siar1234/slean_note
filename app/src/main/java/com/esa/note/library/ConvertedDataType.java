package com.esa.note.library;

public class ConvertedDataType {

    public static String String(int i) {
        return ""+i;
    }
    public static int Integer(String string) {
        int result = 0;
        char[] text = string.toCharArray();
        int sex = 1;
        for (int i = 0; i <= string.length() ; i ++) {
            int index = string.length() - i;
            int plus;
            sex = sex * 10;
            switch (text[i]) {
                case '1':
                    plus = 1;
                    result = result + (plus * sex );
                    break;
                case '2':
                    plus = 2;
                    result = result + (plus * sex );
                    break;
                case '3':
                    plus = 3;
                    result = result + (plus * sex );
                    break;
                case '4':
                    plus = 4;
                    result = result + (plus * sex );
                    break;
                case '5':
                    plus = 5;
                    result = result + (plus * sex );
                    break;
                case '6':
                    plus = 6;
                    result = result + (plus * sex );
                    break;
                case '7':
                    plus = 7;
                    result = result + (plus * sex );
                    break;
                case '8':
                    plus = 8;
                    result = result + (plus * sex );
                    break;
                case '9':
                    plus = 9;
                    result = result + (plus * sex );
                    break;
                case '-':
                    if (index == string.length()) {
                        result = -result;
                    }
                    else {
                        sex = 1;
                    }
                    break;
                default:
                    break;
            }
        }
        return result;
    }
}
